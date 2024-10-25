package com.richatt.m2epayment.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SoldeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Solde getSoldeSample1() {
        return new Solde().id(1L).clientRef("clientRef1").clientName("clientName1").clientFirstname("clientFirstname1");
    }

    public static Solde getSoldeSample2() {
        return new Solde().id(2L).clientRef("clientRef2").clientName("clientName2").clientFirstname("clientFirstname2");
    }

    public static Solde getSoldeRandomSampleGenerator() {
        return new Solde()
            .id(longCount.incrementAndGet())
            .clientRef(UUID.randomUUID().toString())
            .clientName(UUID.randomUUID().toString())
            .clientFirstname(UUID.randomUUID().toString());
    }
}
