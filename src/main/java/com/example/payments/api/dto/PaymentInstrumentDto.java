package com.example.payments.api.dto;

import jakarta.validation.constraints.NotBlank;

public class PaymentInstrumentDto {

    @NotBlank
    private String type;

    private String cardNumber;
    private String expiry;
    private String upiId;

    // getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getExpiry() { return expiry; }
    public void setExpiry(String expiry) { this.expiry = expiry; }
    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }
}
