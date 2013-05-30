package com.pragmasoft.test.traffic.actors;

import com.pragmasoft.test.traffic.messages.command.CommandMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

import static com.pragmasoft.test.traffic.messages.command.CommandEnum.SHUTDOWN;

public abstract class Actor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Actor.class);

    private final String id;
    private final BlockingQueue<CommandMessage> commandQueue;

    public Actor(String id, BlockingQueue<CommandMessage> commandQueue) {
        this.id = id;
        this.commandQueue = commandQueue;
    }

    public String getId() {
        return id;
    }

    @Override
    public void run() {
        logger.info("Actor {} Starting execution", getId());
        boolean shutdown = false;
        do {
            try {
                CommandMessage commandMessage = commandQueue.take();

                if(isShutdown(commandMessage)) {
                    shutdown = true;
                } else {
                    processMessage(commandMessage);
                }
            } catch (InterruptedException e) {
                logger.warn("Interrupted, ending execution");
                shutdown = true;
                Thread.currentThread().interrupt();
            }
        } while(!shutdown);

        logger.info("Actor {} Ending execution", getId());
    }

    // Accessible for unit test only
    abstract void processMessage(CommandMessage commandMessage);

    private boolean isShutdown(CommandMessage commandMessage) {
        return commandMessage.command() == SHUTDOWN;
    }
}
