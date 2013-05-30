package com.pragmasoft.test.traffic.messages.command;

import com.pragmasoft.test.traffic.data.TrafficInfo;

public class DisplayTrafficInfoCommand implements CommandMessage<TrafficInfo> {
    private final TrafficInfo trafficInfo;

    public DisplayTrafficInfoCommand(TrafficInfo trafficInfo) {
        this.trafficInfo = trafficInfo;
    }

    @Override
    public CommandEnum command() {
        return CommandEnum.ECHO;
    }

    @Override
    public TrafficInfo getPayload() {
        return trafficInfo;
    }
}
