package org.is70002024.market.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MarketOverviewTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MarketOverview getMarketOverviewSample1() {
        return new MarketOverview().id(1L).name("name1");
    }

    public static MarketOverview getMarketOverviewSample2() {
        return new MarketOverview().id(2L).name("name2");
    }

    public static MarketOverview getMarketOverviewRandomSampleGenerator() {
        return new MarketOverview().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
