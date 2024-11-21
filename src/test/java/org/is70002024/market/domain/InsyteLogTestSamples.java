package org.is70002024.market.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InsyteLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InsyteLog getInsyteLogSample1() {
        return new InsyteLog().id(1L).name("name1").activity("activity1");
    }

    public static InsyteLog getInsyteLogSample2() {
        return new InsyteLog().id(2L).name("name2").activity("activity2");
    }

    public static InsyteLog getInsyteLogRandomSampleGenerator() {
        return new InsyteLog().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).activity(UUID.randomUUID().toString());
    }
}
