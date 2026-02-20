package com.example.payments.routing;

import com.example.payments.health.GatewayHealthRegistry;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
public class GatewayRouter {

    private final GatewayHealthRegistry healthRegistry;
    private final SecureRandom random = new SecureRandom();

    private final List<GatewayConfig> configs = List.of(
            new GatewayConfig("razorpay", 50),
            new GatewayConfig("payu", 30),
            new GatewayConfig("cashfree", 20)
    );

    public GatewayRouter(GatewayHealthRegistry healthRegistry) {
        this.healthRegistry = healthRegistry;
    }

    public String selectGateway() {
        List<GatewayConfig> healthyConfigs = new ArrayList<>();
        for (GatewayConfig c : configs) {
            if (c.getWeight() > 0 && healthRegistry.isHealthy(c.getName())) {
                healthyConfigs.add(c);
            }
        }
        if (healthyConfigs.isEmpty()) {
            healthyConfigs.addAll(configs);
        }

        int totalWeight = healthyConfigs.stream().mapToInt(GatewayConfig::getWeight).sum();
        int r = random.nextInt(totalWeight);
        int cumulative = 0;

        for (GatewayConfig cfg : healthyConfigs) {
            cumulative += cfg.getWeight();
            if (r < cumulative) {
                return cfg.getName();
            }
        }
        return healthyConfigs.get(0).getName();
    }
}
