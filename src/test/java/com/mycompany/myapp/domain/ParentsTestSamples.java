package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ParentsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Parents getParentsSample1() {
        return new Parents().id(1L).parentsFristName("parentsFristName1").parentsLastName("parentsLastName1");
    }

    public static Parents getParentsSample2() {
        return new Parents().id(2L).parentsFristName("parentsFristName2").parentsLastName("parentsLastName2");
    }

    public static Parents getParentsRandomSampleGenerator() {
        return new Parents()
            .id(longCount.incrementAndGet())
            .parentsFristName(UUID.randomUUID().toString())
            .parentsLastName(UUID.randomUUID().toString());
    }
}
