package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BalanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Balance getBalanceSample1() {
        return new Balance().id(1L).creditCardType("creditCardType1").creditcardNum(1).cvs(1);
    }

    public static Balance getBalanceSample2() {
        return new Balance().id(2L).creditCardType("creditCardType2").creditcardNum(2).cvs(2);
    }

    public static Balance getBalanceRandomSampleGenerator() {
        return new Balance()
            .id(longCount.incrementAndGet())
            .creditCardType(UUID.randomUUID().toString())
            .creditcardNum(intCount.incrementAndGet())
            .cvs(intCount.incrementAndGet());
    }
}
