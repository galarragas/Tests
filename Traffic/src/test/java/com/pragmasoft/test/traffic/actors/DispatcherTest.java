package com.pragmasoft.test.traffic.actors;

import com.google.common.collect.Maps;
import com.pragmasoft.test.traffic.data.Move;
import com.pragmasoft.test.traffic.data.Point;
import com.pragmasoft.test.traffic.messages.command.CommandEnum;
import com.pragmasoft.test.traffic.messages.command.CommandMessage;
import com.pragmasoft.test.traffic.messages.command.DispatchPositionCommand;
import com.pragmasoft.test.traffic.messages.command.MoveCommand;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class DispatcherTest {

    static final String ROBOT_1 = "robot1";
    static final String ROBOT_2 = "robot2";
    private Dispatcher dispatcher;
    private BlockingQueue<CommandMessage> commandQueue;
    private Map<String, BlockingQueue<CommandMessage>> robotsMap;
    private BlockingQueue<CommandMessage> agent1Queue;
    private BlockingQueue<CommandMessage> agent2Queue;

    @Before
    public void setUp() {
        robotsMap = Maps.newHashMap();
        agent1Queue = new LinkedBlockingQueue<CommandMessage>();
        agent2Queue = new LinkedBlockingQueue<CommandMessage>();
        commandQueue = new LinkedBlockingQueue<CommandMessage>();

        robotsMap.put(ROBOT_1, agent1Queue);
        robotsMap.put(ROBOT_2, agent2Queue);
        dispatcher = new Dispatcher("test", commandQueue, robotsMap, 1);
    }

    @Test
    public void shouldForwardToRightRobot() {
        final DispatchPositionCommand commandMessage = new DispatchPositionCommand(new Move(ROBOT_1, new DateTime().withHourOfDay(7), new Point(1.0, 1.0)));
        dispatcher.processMessage(commandMessage);

        assertFalse(agent1Queue.isEmpty());
        assertTrue("Message sent to non-matching agents", agent2Queue.isEmpty());
    }


    @Test
    public void shouldNotSendMessageAfterShutDownToAgent() {
        final DispatchPositionCommand commandMessage = new DispatchPositionCommand(new Move(ROBOT_1, new DateTime().withHourOfDay(9), new Point(1.0, 1.0)));
        dispatcher.processMessage(commandMessage);

        assertFalse("Message shouldn't be sent if after shut down time", agent1Queue.poll().command() == CommandEnum.MOVE);
    }

    @Test
    public void shouldSendBatch() {
        dispatcher = new Dispatcher("test", commandQueue, robotsMap, 2);
        final DispatchPositionCommand commandMessage = new DispatchPositionCommand(new Move(ROBOT_1, new DateTime().withHourOfDay(7), new Point(1.0, 1.0)));
        dispatcher.processMessage(commandMessage);
        dispatcher.processMessage(commandMessage);

        MoveCommand moveCommand = (MoveCommand)agent1Queue.poll();
        assertEquals(Arrays.asList(commandMessage.getPayload(), commandMessage.getPayload()), moveCommand.getPayload());

    }

    @Test
    public void shouldShutDownAgent() {
        final DispatchPositionCommand commandMessage = new DispatchPositionCommand(new Move(ROBOT_1, new DateTime().withHourOfDay(9), new Point(1.0, 1.0)));
        dispatcher.processMessage(commandMessage);

        assertEquals("Expecting shut down command", CommandEnum.SHUTDOWN, agent1Queue.poll().command());
    }

    @Test
    public void shouldNotSendShutDownMessageTwice() {
        final DispatchPositionCommand commandMessage = new DispatchPositionCommand(new Move(ROBOT_1, new DateTime().withHourOfDay(9), new Point(1.0, 1.0)));
        dispatcher.processMessage(commandMessage);

        //Removing shut down message
        agent1Queue.poll();

        dispatcher.processMessage(commandMessage);

        assertTrue(agent1Queue.isEmpty());

    }


}
