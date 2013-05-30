package com.pragmasoft.test.traffic.data;

import org.joda.time.DateTime;

public class TrafficInfo {
    private final String agentId;
    private final DateTime dateTime;
    private final double speed;
    private final TrafficCondition trafficCondition;

    public TrafficInfo(String agentId, DateTime dateTime, double speed, TrafficCondition trafficCondition) {
        this.agentId = agentId;
        this.dateTime = dateTime;
        this.speed = speed;
        this.trafficCondition = trafficCondition;
    }

    public String getAgentId() {
        return agentId;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public double getSpeed() {
        return speed;
    }

    public TrafficCondition getTrafficCondition() {
        return trafficCondition;
    }

    @Override
    public String toString() {
        return "TrafficInfo{" +
                "agentId='" + agentId + '\'' +
                ", dateTime=" + dateTime +
                ", speed=" + speed +
                ", trafficCondition=" + trafficCondition +
                '}';
    }
}
