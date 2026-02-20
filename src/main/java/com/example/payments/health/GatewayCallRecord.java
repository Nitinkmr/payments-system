package com.example.payments.health;

import java.time.Instant;

public class GatewayCallRecord {
    private final Instant timestamp;
    private final boolean success;

    public GatewayCallRecord(Instant timestamp, boolean success) {
        this.timestamp = timestamp;
        this.success = success;
    }

    public Instant getTimestamp() { return timestamp; }
    public boolean isSuccess() { return success; }
}
