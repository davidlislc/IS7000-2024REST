package org.is70002024.market.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transaction getTransactionSample1() {
        return new Transaction().id(1L).name("name1").type("type1");
    }

    public static Transaction getTransactionSample2() {
        return new Transaction().id(2L).name("name2").type("type2");
    }

    public static Transaction getTransactionRandomSampleGenerator() {
        return new Transaction().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).type(UUID.randomUUID().toString());
    }
}
