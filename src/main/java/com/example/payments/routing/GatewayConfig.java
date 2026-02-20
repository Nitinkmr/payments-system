package com.example.payments.routing;

public class GatewayConfig {
    private final String name;
    private final int weight;

    public GatewayConfig(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() { return name; }
    public int getWeight() { return weight; }
}
