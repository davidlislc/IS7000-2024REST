package org.is70002024.market.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GiftcardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Giftcard getGiftcardSample1() {
        return new Giftcard().id(1L).name("name1");
    }

    public static Giftcard getGiftcardSample2() {
        return new Giftcard().id(2L).name("name2");
    }

    public static Giftcard getGiftcardRandomSampleGenerator() {
        return new Giftcard().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
