package com.example.payments.gateway.impl;

import com.example.payments.gateway.PaymentGateway;
import org.springframework.stereotype.Component;

@Component
public class CashfreeGateway implements PaymentGateway {
    @Override
    public String getName() { return "cashfree"; }
}
