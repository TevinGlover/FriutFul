package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PointsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Points getPointsSample1() {
        return new Points().id(1L).amount(1).used(1);
    }

    public static Points getPointsSample2() {
        return new Points().id(2L).amount(2).used(2);
    }

    public static Points getPointsRandomSampleGenerator() {
        return new Points().id(longCount.incrementAndGet()).amount(intCount.incrementAndGet()).used(intCount.incrementAndGet());
    }
}
