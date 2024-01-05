package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChildTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Child getChildSample1() {
        return new Child().id(1L).userFristName("userFristName1").userLastName("userLastName1");
    }

    public static Child getChildSample2() {
        return new Child().id(2L).userFristName("userFristName2").userLastName("userLastName2");
    }

    public static Child getChildRandomSampleGenerator() {
        return new Child()
            .id(longCount.incrementAndGet())
            .userFristName(UUID.randomUUID().toString())
            .userLastName(UUID.randomUUID().toString());
    }
}
