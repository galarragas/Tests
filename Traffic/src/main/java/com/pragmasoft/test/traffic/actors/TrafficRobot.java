package com.pragmasoft.test.traffic.actors;

import com.pragmasoft.test.traffic.data.Move;
import com.pragmasoft.test.traffic.data.Point;
import com.pragmasoft.test.traffic.data.TrafficCondition;
import com.pragmasoft.test.traffic.data.TrafficInfo;
import com.pragmasoft.test.traffic.messages.command.CommandEnum;
import com.pragmasoft.test.traffic.messages.command.CommandMessage;
import com.pragmasoft.test.traffic.messages.command.DisplayTrafficInfoCommand;
import com.pragmasoft.test.traffic.messages.command.MoveCommand;
import com.pragmasoft.test.traffic.poi.PoiRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import static com.pragmasoft.test.traffic.data.TrafficCondition.HEAVY;

public class TrafficRobot extends Actor {
    private static final Logger logger = LoggerFactory.getLogger(TrafficRobot.class);
    static final int FROM_METERS_PER_MILLIS_TO_KM_H_FACTOR = 60 * 60;
    static final int RADIUS_IN_METERS = 350;

    private final Queue<CommandMessage> trafficInfoQueue;
    private final PoiRepository tubeStationRepository;

    private Move lastPosition;

    public TrafficRobot(final String id, final BlockingQueue<CommandMessage> commandQueue,
                        final Queue<CommandMessage> trafficInfoQueue, final PoiRepository tubeStationRepository) {
        super(id, commandQueue);

        this.trafficInfoQueue = trafficInfoQueue;
        this.tubeStationRepository = tubeStationRepository;
        lastPosition = new Move(getId(), new DateTime(0l), new Point(0.0, 0.0));
    }


    @Override
    void processMessage(CommandMessage commandMessage) {
        if (commandMessage.command() != CommandEnum.MOVE) {
            logger.warn("Unsupported message {} received. Discarding", commandMessage.command());
        }

        for (Move currentMove : ((MoveCommand) commandMessage).getPayload()) {
            move(currentMove);
        }
    }

    private void move(Move newPosition) {
        logger.debug("Time {}: Moving to {}", newPosition.getDateTime(), newPosition.getTargetLocation());

        if (tubeStationRepository.existPoiInRadius(newPosition.getTargetLocation(), RADIUS_IN_METERS)) {
            logger.debug("Close to station, sending traffic info");
            trafficInfoQueue.add(new DisplayTrafficInfoCommand(generateTrafficInfo(lastPosition, newPosition)));
        }

        lastPosition = newPosition;
    }

    private TrafficInfo generateTrafficInfo(Move lastPosition, Move newPosition) {
        return new TrafficInfo(getId(), newPosition.getDateTime(), calculateSpeed(lastPosition, newPosition), getTrafficCondition());
    }

    private TrafficCondition getTrafficCondition() {
        return HEAVY;
    }

    private double calculateSpeed(Move lastPosition, Move newPosition) {
        final long moveElapsedTimeInMillis = newPosition.getDateTime().getMillis() - lastPosition.getDateTime().getMillis();
        final double distanceInMeters = lastPosition.getTargetLocation().distanceTo(newPosition.getTargetLocation());

        return (distanceInMeters * FROM_METERS_PER_MILLIS_TO_KM_H_FACTOR) / moveElapsedTimeInMillis;
    }
}
