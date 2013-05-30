package com.pragmasoft.test.springws.persistence;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private AtomicInteger nextId = new AtomicInteger(0);

    public int getNewId() {
        return nextId.incrementAndGet();
    }
}
