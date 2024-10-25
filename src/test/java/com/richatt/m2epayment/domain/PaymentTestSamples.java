package com.richatt.m2epayment.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Payment getPaymentSample1() {
        return new Payment()
            .id(1L)
            .transactionId("transactionId1")
            .phoneNumber("phoneNumber1")
            .clientRef("clientRef1")
            .walletMessage("walletMessage1")
            .sapMessage("sapMessage1")
            .payWallet("payWallet1")
            .payWalletStatus("payWalletStatus1")
            .paySapStatus("paySapStatus1");
    }

    public static Payment getPaymentSample2() {
        return new Payment()
            .id(2L)
            .transactionId("transactionId2")
            .phoneNumber("phoneNumber2")
            .clientRef("clientRef2")
            .walletMessage("walletMessage2")
            .sapMessage("sapMessage2")
            .payWallet("payWallet2")
            .payWalletStatus("payWalletStatus2")
            .paySapStatus("paySapStatus2");
    }

    public static Payment getPaymentRandomSampleGenerator() {
        return new Payment()
            .id(longCount.incrementAndGet())
            .transactionId(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .clientRef(UUID.randomUUID().toString())
            .walletMessage(UUID.randomUUID().toString())
            .sapMessage(UUID.randomUUID().toString())
            .payWallet(UUID.randomUUID().toString())
            .payWalletStatus(UUID.randomUUID().toString())
            .paySapStatus(UUID.randomUUID().toString());
    }
}
