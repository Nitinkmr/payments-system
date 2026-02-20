package com.example.payments.health;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;

public class GatewayStats {

    private final String gatewayName;
    private final Deque<GatewayCallRecord> callHistory = new ArrayDeque<>();
    private Instant unhealthyUntil;
    private final Duration slidingWindow;
    private final double successRateThreshold;

    public GatewayStats(String gatewayName, Duration slidingWindow, double successRateThreshold) {
        this.gatewayName = gatewayName;
        this.slidingWindow = slidingWindow;
        this.successRateThreshold = successRateThreshold;
    }

    public synchronized void recordCall(boolean success) {
        callHistory.addLast(new GatewayCallRecord(Instant.now(), success));
        removeOldCalls();
        evaluateHealth();
    }

    private void removeOldCalls() {
        Instant now = Instant.now();
        while (!callHistory.isEmpty()) {
            GatewayCallRecord rec = callHistory.peekFirst();
            if (Duration.between(rec.getTimestamp(), now).compareTo(slidingWindow) > 0) {
                callHistory.removeFirst();
            } else {
                break;
            }
        }
    }

    private void evaluateHealth() {
        if (callHistory.isEmpty()) return;

        int total = callHistory.size();
        int successCount = 0;
        for (GatewayCallRecord rec : callHistory) {
            if (rec.isSuccess()) successCount++;
        }
        double successRate = (double) successCount / total;
        if (total >= 10 && successRate < successRateThreshold) {
            unhealthyUntil = Instant.now().plus(Duration.ofMinutes(30));
        }
    }

    public synchronized boolean isHealthy() {
        if (unhealthyUntil == null) return true;
        if (Instant.now().isAfter(unhealthyUntil)) {
            unhealthyUntil = null;
            return true;
        }
        return false;
    }

    public String getGatewayName() { return gatewayName; }
}
