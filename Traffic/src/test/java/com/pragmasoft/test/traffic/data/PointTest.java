package com.pragmasoft.test.traffic.data;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class PointTest {

    @Test
    public void shouldBeZeroDistanceForSamePoint() {
        final Point point = new Point(10.23, -45.1234);
        assertEquals(0, point.distanceTo(point));
    }

    @Test
    public void shouldCalculateDistance() {
        final Point p1 = new Point(51.54443, -0.15482);
        final Point p2 = new Point(51.54212, -0.1744);

        assertEquals(1378, p1.distanceTo(p2));

    }
}
