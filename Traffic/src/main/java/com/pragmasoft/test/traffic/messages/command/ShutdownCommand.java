package com.pragmasoft.test.traffic.messages.command;

import static com.pragmasoft.test.traffic.messages.command.CommandEnum.*;

public class ShutdownCommand implements CommandMessage<Void> {
    @Override
    public CommandEnum command() {
        return SHUTDOWN;
    }

    @Override
    public Void getPayload() {
        return null;
    }
}
