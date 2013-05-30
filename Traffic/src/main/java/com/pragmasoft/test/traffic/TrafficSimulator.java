package com.pragmasoft.test.traffic;

import com.google.common.collect.Maps;
import com.pragmasoft.test.traffic.actors.Dispatcher;
import com.pragmasoft.test.traffic.actors.EchoActor;
import com.pragmasoft.test.traffic.actors.TrafficRobot;
import com.pragmasoft.test.traffic.data.Point;
import com.pragmasoft.test.traffic.messages.command.CommandMessage;
import com.pragmasoft.test.traffic.messages.command.ShutdownCommand;
import com.pragmasoft.test.traffic.poi.InMemoryTubeStationRepository;
import com.pragmasoft.test.traffic.poi.PoiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TrafficSimulator {
    private static final Logger logger = LoggerFactory.getLogger(TrafficSimulator.class);
    static final String ROBOT1_ID = "5937";
    static final String ROBOT2_ID = "6043";

    private final PoiRepository tubeRepository;

    private final BlockingQueue<CommandMessage> robot1Queue;
    private final BlockingQueue<CommandMessage> robot2Queue;
    private final BlockingQueue<CommandMessage> dispatcherQueue;
    private final BlockingQueue<CommandMessage> echoQueue;

    private final TrafficRobot trafficRobot1;
    private final TrafficRobot trafficRobot2;

    private final Dispatcher dispatcher;
    private final EchoActor echoActor;

    private PositionReader taxi1PositionReader;
    private PositionReader taxi2PositionReader;

    ExecutorService systemActorsPool = Executors.newFixedThreadPool(2);
    ExecutorService robotPool = Executors.newFixedThreadPool(2);
    ExecutorService readersPool = Executors.newFixedThreadPool(2);

    TrafficSimulator() {
        tubeRepository = new InMemoryTubeStationRepository();

        robot1Queue = new LinkedBlockingQueue<CommandMessage>();
        robot2Queue = new LinkedBlockingQueue<CommandMessage>();
        dispatcherQueue = new LinkedBlockingQueue<CommandMessage>();
        echoQueue = new LinkedBlockingQueue<CommandMessage>();

        trafficRobot1 = new TrafficRobot(ROBOT1_ID, robot1Queue, echoQueue, tubeRepository);
        trafficRobot2 = new TrafficRobot(ROBOT2_ID, robot2Queue, echoQueue, tubeRepository);

        echoActor = new EchoActor("Echo", echoQueue);

        Map<String, BlockingQueue<CommandMessage>> robotMap = Maps.newHashMap();
        robotMap.put(ROBOT1_ID, robot1Queue);
        robotMap.put(ROBOT2_ID, robot2Queue);

        dispatcher = new Dispatcher("Dispatcher", dispatcherQueue, robotMap, 10);

    }

    private void doSimulation() throws InterruptedException {
        submitToPool(readersPool, taxi2PositionReader, taxi1PositionReader);

        readersPool.shutdown();
        readersPool.awaitTermination(40, TimeUnit.SECONDS);
        robotPool.shutdown();
        robotPool.awaitTermination(40, TimeUnit.SECONDS);
    }


    private void shutDown() throws InterruptedException {

        logger.info("Shuttind down Dispatcher");
        dispatcherQueue.put(new ShutdownCommand());

        logger.info("Shuttind down Echo Agent");
        echoQueue.put(new ShutdownCommand());

        systemActorsPool.shutdown();
        logger.info("Awaiting termination");
        systemActorsPool.awaitTermination(20, TimeUnit.SECONDS);

        logger.info("Killing threads if any left dangling");
        List<Runnable> unterminatedRobots = robotPool.shutdownNow();
        if(!unterminatedRobots.isEmpty()) {
            logger.warn("Had to kill robots: {}", unterminatedRobots);
        }

        List<Runnable> unterminatedReaders = readersPool.shutdownNow();
        if(!unterminatedReaders.isEmpty()) {
            logger.warn("Had to kill readers: {}", unterminatedReaders);
        }

        List<Runnable> unterminatedSystemActors = systemActorsPool.shutdownNow();
        if(!unterminatedSystemActors.isEmpty()) {
            logger.warn("Had to kill system actors: {}", unterminatedSystemActors);
        }
    }

    private void init() throws IOException {
        loadPoi();

        logger.info("Starting system actors");
        submitToPool(systemActorsPool, echoActor, dispatcher);

        logger.info("Starting robots");
        submitToPool(robotPool, trafficRobot1, trafficRobot2);

        logger.info("Initing taxi position reader");
        taxi1PositionReader = new PositionReader(
                new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("robot/" + ROBOT1_ID + ".csv"))),
                dispatcherQueue
        );
        taxi2PositionReader = new PositionReader(
                new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("robot/" + ROBOT2_ID + ".csv"))),
                dispatcherQueue
        );
    }

    private void loadPoi() throws IOException {
        logger.info("Loading POIs");
        BufferedReader poiReader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("tube/tube.csv")));

        String nextLine = poiReader.readLine();
        while (nextLine != null) {
            final String[] tokens = nextLine.replaceAll("\"", "").split(",");
            final String description = tokens[0];
            final Point location = new Point(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
            tubeRepository.addPoi(location, description);

            nextLine = poiReader.readLine();
        }
    }

    private void submitToPool(ExecutorService pool, Runnable... runnables) {
        for(Runnable runnable : runnables) {
            pool.submit(runnable);
        }
    }


    public static void main(String[] argv) throws Exception {
        TrafficSimulator simulator = new TrafficSimulator();

        logger.info("Initialising");
        simulator.init();

        try {

            logger.info("Starting simulation");
            simulator.doSimulation();
        } finally {
            simulator.shutDown();
            logger.info("done");
        }
    }

}

