package com.example.payments.gateway;

public class CallbackNormalizedPayload {
    private String orderId;
    private boolean success;
    private String gatewayName;
    private String reason;

    public CallbackNormalizedPayload() {}

    public CallbackNormalizedPayload(String orderId, boolean success, String gatewayName, String reason) {
        this.orderId = orderId;
        this.success = success;
        this.gatewayName = gatewayName;
        this.reason = reason;
    }

    // getters and setters
}
