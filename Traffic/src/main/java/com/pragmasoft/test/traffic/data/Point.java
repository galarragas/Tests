package com.pragmasoft.test.traffic.data;

public class Point {
    public static final double DEGREES_TO_METER_FACTOR = 60 * 1.1515 * 1609.344;
    private final double latitude;
    private final double longitude;

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Copied from the internet, should use a proper GIS library
    public int distanceTo(Point that) {
        double theta = this.longitude - that.longitude;
        double dist = Math.sin(deg2rad(this.latitude)) * Math.sin(deg2rad(that.latitude))
                + Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(that.latitude)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * DEGREES_TO_METER_FACTOR;

        return (int)Math.floor(dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.latitude, latitude) != 0) return false;
        if (Double.compare(point.longitude, longitude) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
