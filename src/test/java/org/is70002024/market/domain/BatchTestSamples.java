package org.is70002024.market.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BatchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Batch getBatchSample1() {
        return new Batch().id(1L).name("name1").job("job1");
    }

    public static Batch getBatchSample2() {
        return new Batch().id(2L).name("name2").job("job2");
    }

    public static Batch getBatchRandomSampleGenerator() {
        return new Batch().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).job(UUID.randomUUID().toString());
    }
}
