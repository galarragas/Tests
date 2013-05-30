package com.pragmasoft.test.traffic;

import com.pragmasoft.test.traffic.data.Move;
import com.pragmasoft.test.traffic.data.Point;
import com.pragmasoft.test.traffic.messages.command.CommandMessage;
import com.pragmasoft.test.traffic.messages.command.DispatchPositionCommand;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class PositionReaderTest {

    @Test
    public void shouldReadNextLineAndSendMessage() {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream("5937,\"51.476105\",\"-0.100224\",\"2011-03-22 07:55:26\"".getBytes())));
        final Move expectedMove = new Move(
                "5937",
                new DateTime()
                        .withYear(2011).withMonthOfYear(3).withDayOfMonth(22)
                        .withHourOfDay(7).withMinuteOfHour(55).withSecondOfMinute(26)
                        .withMillisOfSecond(0),
                new Point(51.476105, -0.100224)
        );
        final Queue<CommandMessage> dispatcherQueue = new LinkedBlockingDeque<CommandMessage>();
        final PositionReader positionReader = new PositionReader(reader, dispatcherQueue);

        positionReader.processStep();

        assertFalse(dispatcherQueue.isEmpty());
        assertEquals(new DispatchPositionCommand(expectedMove), dispatcherQueue.poll());
    }

    @Test
    public void shouldCloseExecutionWhenStreamIsEmpty() {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        final Queue<CommandMessage> dispatcherQueue = new LinkedBlockingDeque<CommandMessage>();
        final PositionReader positionReader = new PositionReader(reader, dispatcherQueue);

        assertFalse(positionReader.processStep());
        assertTrue(dispatcherQueue.isEmpty());
    }
}
