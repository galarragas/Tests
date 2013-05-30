package com.pragmasoft.test.traffic.actors;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.pragmasoft.test.traffic.data.Move;
import com.pragmasoft.test.traffic.messages.command.CommandMessage;
import com.pragmasoft.test.traffic.messages.command.MoveCommand;
import com.pragmasoft.test.traffic.messages.command.ShutdownCommand;
import com.sun.istack.internal.Nullable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import static junit.framework.Assert.assertTrue;

public class ActorTest {

    private final BlockingQueue<CommandMessage> commandQueue = new LinkedBlockingDeque<CommandMessage>();
    private final StubActor actor = new StubActor("testActor", commandQueue);

    private final ExecutorService pool = Executors.newFixedThreadPool(1);

    @Before
    public void setUp() {
        pool.submit(actor);
    }

    @After
    public void shutDown() {
        pool.shutdownNow();
    }

    @Test
    public void shouldProcessMessages() throws InterruptedException {
        final CommandMessage commandMessage = new MoveCommand(new ArrayList<Move>());
        commandQueue.put(commandMessage);

        assertTrueWithTimeout(1000, new Predicate<Void>() {
            @Override
            public boolean apply(@Nullable java.lang.Void input) {
                return actor.processedMessages.contains(commandMessage);
            }
        });
    }

    @Test
    public void shouldShutDownWithShutdownMessage() throws InterruptedException {
        commandQueue.put(new ShutdownCommand());

        assertTrueWithTimeout(1000, new Predicate<Void>() {
            @Override
            public boolean apply(@Nullable java.lang.Void input) {
                pool.shutdown();
                return pool.isTerminated();
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Test
    public void shouldTerminateWhenInterrupted() throws InterruptedException {
        final Thread thread = new Thread(new StubActor("testActor", new LinkedBlockingDeque<CommandMessage>()));

        try {
            thread.start();
            thread.interrupt();

            assertTrueWithTimeout(1000, new Predicate<Void>() {
                @Override
                public boolean apply(@Nullable java.lang.Void input) {
                    return !thread.isAlive();
                }
            });
        } finally {
            thread.stop();
        }
    }

    private static class StubActor extends Actor {
        final List<CommandMessage> processedMessages = Lists.newArrayList();

        public StubActor(String id, BlockingQueue<CommandMessage> commandQueue) {
            super(id, commandQueue);
        }

        @Override
        void processMessage(CommandMessage commandMessage) {
            processedMessages.add(commandMessage);
        }
    }

    private void assertTrueWithTimeout(int timeout, Predicate<Void> predicate) throws InterruptedException {
        if (!predicate.apply(null)) {
            Thread.sleep(timeout);
            assertTrue(predicate.apply(null));
        }
    }

}
