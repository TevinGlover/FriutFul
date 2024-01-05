package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CreditScoreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CreditScore getCreditScoreSample1() {
        return new CreditScore().id(1L).scoreNumber(1);
    }

    public static CreditScore getCreditScoreSample2() {
        return new CreditScore().id(2L).scoreNumber(2);
    }

    public static CreditScore getCreditScoreRandomSampleGenerator() {
        return new CreditScore().id(longCount.incrementAndGet()).scoreNumber(intCount.incrementAndGet());
    }
}
