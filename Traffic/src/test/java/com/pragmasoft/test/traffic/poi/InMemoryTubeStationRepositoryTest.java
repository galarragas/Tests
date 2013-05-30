package com.pragmasoft.test.traffic.poi;

import com.pragmasoft.test.traffic.data.Point;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class InMemoryTubeStationRepositoryTest {

    private Point point;
    private InMemoryTubeStationRepository repository;

    @Before
    public void setUp() throws Exception {
        point = new Point(51.54443, -0.15482);
        repository = new InMemoryTubeStationRepository();
        repository.addPoi(point, "Test POI");
    }

    @Test
    public void shouldFindExistingPoi() {
        assertTrue(repository.existPoiInRadius(point, 0));
    }

    @Test
    public void shouldNotFindNotExistingPoi() {
        assertFalse(repository.existPoiInRadius(new Point(100.212, -0.15482), 0));
    }


    @Test
    public void shouldFindPoiInRadius() {
        assertTrue(repository.existPoiInRadius(new Point(51.54212, -0.1744), 1400));
    }

    @Test
    public void shouldNotFindPoiOutOfRadius() {
        assertFalse(repository.existPoiInRadius(new Point(51.54212, -0.1744), 1300));
    }
}
