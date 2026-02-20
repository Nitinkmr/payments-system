package com.example.payments.gateway;

public interface PaymentGateway {

    String getName();

    default void initiatePayment(String transactionId, double amount) {
        // mock
    }
}
