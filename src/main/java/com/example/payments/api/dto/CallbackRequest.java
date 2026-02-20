package com.example.payments.api.dto;

import jakarta.validation.constraints.NotBlank;

public class CallbackRequest {

    @NotBlank
    private String orderId;

    @NotBlank
    private String status;  // success | failure

    @NotBlank
    private String gateway;

    private String reason;

    // getters and setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getGateway() { return gateway; }
    public void setGateway(String gateway) { this.gateway = gateway; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
