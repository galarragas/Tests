package com.pragmasoft.test.traffic.actors;

import com.google.common.collect.Lists;
import com.pragmasoft.test.traffic.data.Move;
import com.pragmasoft.test.traffic.data.Point;
import com.pragmasoft.test.traffic.data.TrafficCondition;
import com.pragmasoft.test.traffic.data.TrafficInfo;
import com.pragmasoft.test.traffic.messages.command.CommandMessage;
import com.pragmasoft.test.traffic.messages.command.MoveCommand;
import com.pragmasoft.test.traffic.poi.PoiRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TrafficRobotTest {
    static final long ONE_HOUR = 1000 * 60 * 60l;
    private BlockingQueue<CommandMessage> commandQueue;
    private Queue<CommandMessage> trafficInfoQueue;

    @Mock
    private PoiRepository mockTubeStationRepository;

    private TrafficRobot trafficRobot;

    private final Point p1 = new Point(51.54443, -0.15482);
    private final Point p2 = new Point(51.54212, -0.1744);

    @Before
    public void setUp() {
        commandQueue = new LinkedBlockingQueue<CommandMessage>();
        trafficInfoQueue = new LinkedBlockingQueue<CommandMessage>();
        trafficRobot = new TrafficRobot("testTrafficRobot", commandQueue, trafficInfoQueue, mockTubeStationRepository);
    }

    @Test
    public void shouldCheckIfCloseToTubeStation() {
        Move move1 = new Move("1", new DateTime(1000l), p1);
        Move move2 = new Move("2", new DateTime(1000l), p2);
        trafficRobot.processMessage(new MoveCommand(Lists.newArrayList(move1, move2)));

        verify(mockTubeStationRepository).existPoiInRadius(eq(p1), anyInt());
        verify(mockTubeStationRepository).existPoiInRadius(eq(p2), anyInt());
    }

    @Test
    public void shouldUseConstantRadius() {
        trafficRobot.processMessage(new MoveCommand(Lists.newArrayList(new Move("1", new DateTime(1000l), p1))));

        verify(mockTubeStationRepository).existPoiInRadius(any(Point.class), eq(TrafficRobot.RADIUS_IN_METERS));
    }

    @Test
    public void shouldGenerateNoEventIfNoMatch() {
        given(mockTubeStationRepository.existPoiInRadius(any(Point.class), anyInt())).willReturn(false);

        trafficRobot.processMessage(new MoveCommand(Lists.newArrayList(new Move("1", new DateTime(1000l), p1))));

        assertTrue(trafficInfoQueue.isEmpty());
    }

    @Test
    public void shouldGenerateEventIfNoMatch() {
        given(mockTubeStationRepository.existPoiInRadius(any(Point.class), anyInt())).willReturn(true);

        trafficRobot.processMessage(new MoveCommand(Lists.newArrayList(new Move("1", new DateTime(ONE_HOUR), p1))));
        trafficRobot.processMessage(new MoveCommand(Lists.newArrayList(new Move("1", new DateTime(2 * ONE_HOUR), p2))));

        TrafficInfo expectedTrafficInfo1 = new TrafficInfo("testTrafficRobot", new DateTime(ONE_HOUR),
                ((double)p1.distanceTo(new Point(0, 0))/1000), TrafficCondition.HEAVY);
        TrafficInfo expectedTrafficInfo2 = new TrafficInfo("testTrafficRobot", new DateTime(2 * ONE_HOUR),
                ((double) p1.distanceTo(p2)/1000), TrafficCondition.HEAVY);

        assertFalse("No event in queue", trafficInfoQueue.isEmpty());
        assertAlmostEqualIgnoringTrafficCondition(expectedTrafficInfo1, (TrafficInfo)trafficInfoQueue.poll().getPayload());
        assertFalse("Expected two events", trafficInfoQueue.isEmpty());
        assertAlmostEqualIgnoringTrafficCondition(expectedTrafficInfo2, (TrafficInfo)trafficInfoQueue.poll().getPayload());
    }

    private void assertAlmostEqualIgnoringTrafficCondition(final TrafficInfo expectedTrafficInfo, final TrafficInfo actual) {
        assertEquals("Wrong agent ID", expectedTrafficInfo.getAgentId(), actual.getAgentId());
        assertEquals("Wrong event time", expectedTrafficInfo.getDateTime(), actual.getDateTime());
        assertEquals("Wrong speed", expectedTrafficInfo.getSpeed(), actual.getSpeed());
    }
}
