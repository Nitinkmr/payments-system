package com.example.payments.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InitiateTransactionRequest {

    @NotBlank
    private String orderId;

    @Min(1)
    private double amount;

    @NotNull
    private PaymentInstrumentDto paymentInstrument;

    // getters and setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public PaymentInstrumentDto getPaymentInstrument() { return paymentInstrument; }
    public void setPaymentInstrument(PaymentInstrumentDto paymentInstrument) { this.paymentInstrument = paymentInstrument; }
}
