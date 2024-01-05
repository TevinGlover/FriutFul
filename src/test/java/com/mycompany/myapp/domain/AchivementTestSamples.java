package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AchivementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Achivement getAchivementSample1() {
        return new Achivement().id(1L).name("name1").pointValue(1);
    }

    public static Achivement getAchivementSample2() {
        return new Achivement().id(2L).name("name2").pointValue(2);
    }

    public static Achivement getAchivementRandomSampleGenerator() {
        return new Achivement().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).pointValue(intCount.incrementAndGet());
    }
}
