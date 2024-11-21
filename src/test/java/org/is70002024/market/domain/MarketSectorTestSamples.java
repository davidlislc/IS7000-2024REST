package org.is70002024.market.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MarketSectorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MarketSector getMarketSectorSample1() {
        return new MarketSector().id(1L);
    }

    public static MarketSector getMarketSectorSample2() {
        return new MarketSector().id(2L);
    }

    public static MarketSector getMarketSectorRandomSampleGenerator() {
        return new MarketSector().id(longCount.incrementAndGet());
    }
}
