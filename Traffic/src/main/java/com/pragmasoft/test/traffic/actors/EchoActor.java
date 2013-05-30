package com.pragmasoft.test.traffic.actors;

import com.pragmasoft.test.traffic.messages.command.CommandEnum;
import com.pragmasoft.test.traffic.messages.command.CommandMessage;
import com.pragmasoft.test.traffic.messages.command.DisplayTrafficInfoCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class EchoActor extends Actor {
    private final Logger logger = LoggerFactory.getLogger(EchoActor.class);

    public EchoActor(String id, BlockingQueue<CommandMessage> commandQueue) {
        super(id, commandQueue);
    }

    @Override
    void processMessage(CommandMessage commandMessage) {
        if(commandMessage.command() == CommandEnum.ECHO) {
            logger.info("ECHO: {}", ((DisplayTrafficInfoCommand)commandMessage).getPayload());
        } else {
            logger.warn("Unsupported message {} received. Discarding", commandMessage.command());
        }
    }
}
