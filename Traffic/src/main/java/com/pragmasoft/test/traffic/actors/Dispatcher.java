package com.pragmasoft.test.traffic.actors;

import com.pragmasoft.test.traffic.data.Move;
import com.pragmasoft.test.traffic.messages.command.CommandEnum;
import com.pragmasoft.test.traffic.messages.command.CommandMessage;
import com.pragmasoft.test.traffic.messages.command.DispatchPositionCommand;
import com.pragmasoft.test.traffic.messages.command.MoveCommand;
import com.pragmasoft.test.traffic.messages.command.ShutdownCommand;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class Dispatcher extends Actor {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private final Map<String, BlockingQueue<CommandMessage>> routingMap;
    private final Set<String> shutDownDestinations;
    private final Map<String, List<Move>> movesBatch = new HashMap<String, List<Move>>();
    private final int batchSize;

    public Dispatcher(String id, BlockingQueue<CommandMessage> commandQueue, Map<String, BlockingQueue<CommandMessage>> routingMap, int batchSize) {
        super(id, commandQueue);
        this.routingMap = routingMap;
        shutDownDestinations = new HashSet<String>();
        this.batchSize = batchSize;
    }

    private BufferedReader openReader(String currRobotID) {
        return new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("robot/" + currRobotID + ".csv")));
    }

    @Override
    void processMessage(CommandMessage commandMessage) {
        if (commandMessage.command() != CommandEnum.DISPATCH) {
            logger.warn("Unsupported message type {}, discarding message", commandMessage.command());
        }

        Move taxiMove = ((DispatchPositionCommand) commandMessage).getPayload();
        if (routingMap.containsKey(taxiMove.getTaxiId())) {
            if (isShutDown(taxiMove)) {
                logger.debug("Message for shut down taxi, ignoring");
                return;
            }

            if (isMessageAfterShutDownTime(taxiMove.getDateTime())) {
                sendLastBatch(taxiMove);
                shutDownTaxi(taxiMove.getTaxiId());
            } else {
                batchSend(taxiMove);
            }
        } else {
            logger.warn("Position for unknown taxi {}, ingoring message", taxiMove.getTaxiId());
        }

    }

    private boolean isShutDown(Move taxiMove) {
        return shutDownDestinations.contains(taxiMove.getTaxiId());
    }

    private void sendLastBatch(Move taxiMove) {
        List<Move> batch = movesBatch.get(taxiMove.getTaxiId());
        if ((batch != null) && (batch.size() > 0)) {
            routingMap.get(taxiMove.getTaxiId()).add(new MoveCommand(batch));
        }
        movesBatch.remove(taxiMove.getTaxiId());
    }

    private void batchSend(Move taxiMove) {
        List<Move> batch = movesBatch.get(taxiMove.getTaxiId());
        if (batch == null) {
            batch = new ArrayList<Move>();
            movesBatch.put(taxiMove.getTaxiId(), batch);
        }

        batch.add(taxiMove);

        if (batch.size() == batchSize) {
            routingMap.get(taxiMove.getTaxiId()).add(new MoveCommand(batch));
            movesBatch.remove(taxiMove.getTaxiId());
        }
    }

    private void shutDownTaxi(String taxiId) {
        shutDownDestinations.add(taxiId);
        routingMap.get(taxiId).offer(new ShutdownCommand());
    }

    private boolean isMessageAfterShutDownTime(DateTime dateTime) {
        final DateTime ignoreDay = new DateTime().withHourOfDay(dateTime.getHourOfDay()).withMinuteOfHour(dateTime.getMinuteOfHour());

        return ignoreDay.isAfter(new DateTime().withHourOfDay(8).withMinuteOfHour(10));
    }
}
