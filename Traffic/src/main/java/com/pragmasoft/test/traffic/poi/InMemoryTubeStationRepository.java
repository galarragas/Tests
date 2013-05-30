package com.pragmasoft.test.traffic.poi;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.pragmasoft.test.traffic.data.Point;
import com.sun.istack.internal.Nullable;

import java.util.List;


public class InMemoryTubeStationRepository implements PoiRepository {
    private static class POI {
        final Point location;
        final String description;

        private POI(Point location, String description) {
            this.location = location;
            this.description = description;
        }
    }

    private final List<POI> elements = Lists.newArrayList();

    @Override
    public void addPoi(Point location, String description) {
        elements.add(new POI(location, description));
    }

    @Override
    public boolean existPoiInRadius(final Point from, final int radiusInMeters) {
        return Iterables.tryFind(elements, isCloserThanTo(radiusInMeters, from)).isPresent();
    }

    private Predicate<POI> isCloserThanTo(final int radiusInMeters, final Point from) {
        return new Predicate<POI>() {
            @Override
            public boolean apply(@Nullable POI input) {
                return (input != null) && (from.distanceTo(input.location) <= radiusInMeters);
            }
        };
    }
}
