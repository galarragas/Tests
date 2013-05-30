package com.pragmasoft.test.traffic.messages.command;

public interface CommandMessage<T> {
    CommandEnum command();

    T getPayload();
}
