package com.pragmasoft.test.traffic.data;

import org.joda.time.DateTime;

public class Move {
    private final String taxiId;
    private final DateTime dateTime;
    private final Point targetLocation;

    public Point getTargetLocation() {
        return targetLocation;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getTaxiId() {
        return taxiId;
    }

    public Move(String taxiId, DateTime dateTime, Point targetLocation) {
        this.taxiId = taxiId;
        this.dateTime = dateTime;
        this.targetLocation = targetLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (dateTime != null ? !dateTime.equals(move.dateTime) : move.dateTime != null) return false;
        if (targetLocation != null ? !targetLocation.equals(move.targetLocation) : move.targetLocation != null)
            return false;
        if (taxiId != null ? !taxiId.equals(move.taxiId) : move.taxiId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = taxiId != null ? taxiId.hashCode() : 0;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (targetLocation != null ? targetLocation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Move{" +
                "taxiId='" + taxiId + '\'' +
                ", dateTime=" + dateTime +
                ", targetLocation=" + targetLocation +
                '}';
    }
}
