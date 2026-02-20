package com.example.payments.health;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GatewayHealthRegistry {

    private final Map<String, GatewayStats> statsByGateway = new ConcurrentHashMap<>();
    private final Duration slidingWindow = Duration.ofMinutes(15);
    private final double successThreshold = 0.90;

    @PostConstruct
    public void init() {}

    public GatewayStats getOrCreateStats(String gatewayName) {
        return statsByGateway.computeIfAbsent(
                gatewayName,
                g -> new GatewayStats(g, slidingWindow, successThreshold)
        );
    }

    public void recordResult(String gatewayName, boolean success) {
        getOrCreateStats(gatewayName).recordCall(success);
    }

    public boolean isHealthy(String gatewayName) {
        return getOrCreateStats(gatewayName).isHealthy();
    }
}
