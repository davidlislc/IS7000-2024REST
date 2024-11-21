package org.is70002024.market.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubscriptionsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Subscriptions getSubscriptionsSample1() {
        return new Subscriptions().id(1L).name("name1").status("status1");
    }

    public static Subscriptions getSubscriptionsSample2() {
        return new Subscriptions().id(2L).name("name2").status("status2");
    }

    public static Subscriptions getSubscriptionsRandomSampleGenerator() {
        return new Subscriptions().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).status(UUID.randomUUID().toString());
    }
}
