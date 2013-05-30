package com.pragmasoft.test.traffic.poi;

import com.pragmasoft.test.traffic.data.Point;

public interface PoiRepository {
    void addPoi(Point location, String description);

    boolean existPoiInRadius(Point from, int radiusInMeters);
}
