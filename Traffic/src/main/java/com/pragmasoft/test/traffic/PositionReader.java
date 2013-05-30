package com.pragmasoft.test.traffic;

import com.pragmasoft.test.traffic.data.Move;
import com.pragmasoft.test.traffic.data.Point;
import com.pragmasoft.test.traffic.messages.command.CommandMessage;
import com.pragmasoft.test.traffic.messages.command.DispatchPositionCommand;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Queue;
import java.util.Random;

public class PositionReader implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PositionReader.class);

    private final BufferedReader positionReader;
    private final Queue<CommandMessage> dispatcherQueue;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    Random random = new Random();

    public PositionReader(BufferedReader positionReader, Queue<CommandMessage> dispatcherQueue) {
        this.positionReader = positionReader;
        this.dispatcherQueue = dispatcherQueue;
    }

    @Override
    public void run() {
        logger.info("Reading positions");
        boolean hasWork;
        do {
            hasWork = processStep();
            if (simulateProcessingDelay()) break;
        }
        while (hasWork);
        logger.info("Done");
    }

    boolean processStep() {
        try {
            String currentLine = positionReader.readLine();
            if (currentLine != null) {
                dispatcherQueue.add(new DispatchPositionCommand(extractMove(currentLine)));
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            logger.error("Error reading from input position file", e);
            return false;
        }
    }

    private Move extractMove(final String currentLine) {
        final String[] tokens = currentLine.replaceAll("\"", "").split(",");

        try {
            return new Move(
                    tokens[0].trim(),
                    dateTimeFormatter.parseDateTime(tokens[3].trim()),
                    new Point(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]))
            );
        } catch (RuntimeException e) {
            logger.error("Exception reading file", e);
            throw e;
        }
    }

    private boolean simulateProcessingDelay() {
        try {
            Thread.sleep(1 + Math.abs(random.nextLong()) % 30);
        } catch (InterruptedException e) {
            logger.warn("Interrupted", e);
            return true;
        }
        return false;
    }
}
