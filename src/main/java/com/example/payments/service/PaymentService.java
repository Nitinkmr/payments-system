package com.example.payments.service;

import com.example.payments.api.dto.CallbackRequest;
import com.example.payments.api.dto.InitiateTransactionRequest;
import com.example.payments.api.dto.TransactionResponse;
import com.example.payments.domain.Transaction;
import com.example.payments.domain.TransactionStatus;
import com.example.payments.gateway.PaymentGateway;
import com.example.payments.health.GatewayHealthRegistry;
import com.example.payments.repository.TransactionRepository;
import com.example.payments.routing.GatewayRouter;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {

    private final TransactionRepository txRepo;
    private final GatewayRouter router;
    private final GatewayHealthRegistry healthRegistry;
    private final Map<String, PaymentGateway> gatewayByName = new ConcurrentHashMap<>();

    public PaymentService(TransactionRepository txRepo,
                          GatewayRouter router,
                          GatewayHealthRegistry healthRegistry,
                          List<PaymentGateway> gateways) {
        this.txRepo = txRepo;
        this.router = router;
        this.healthRegistry = healthRegistry;
        for (PaymentGateway g : gateways) {
            gatewayByName.put(g.getName(), g);
        }
    }

    public TransactionResponse initiateTransaction(InitiateTransactionRequest req) {
        String gatewayName = router.selectGateway();
        List<Transaction> existing = txRepo.findByOrderId(req.getOrderId());
        int attempt = existing.size() + 1;

        Transaction tx = new Transaction(req.getOrderId(), req.getAmount(), gatewayName, attempt);
        txRepo.save(tx);

        PaymentGateway gw = gatewayByName.get(gatewayName);
        if (gw != null) {
            gw.initiatePayment(tx.getId(), tx.getAmount());
        }

        return toResponse(tx);
    }

    public void handleCallback(CallbackRequest callback) {
        String gatewayName = callback.getGateway();
        List<Transaction> allForOrder = txRepo.findByOrderId(callback.getOrderId());
        Transaction latestForGateway = allForOrder.stream()
                .filter(t -> t.getGatewayName().equalsIgnoreCase(gatewayName))
                .reduce((first, second) -> second)
                .orElse(null);

        if (latestForGateway == null) {
            return;
        }

        boolean success = "success".equalsIgnoreCase(callback.getStatus());
        latestForGateway.setStatus(success ? TransactionStatus.SUCCESS : TransactionStatus.FAILURE);
        latestForGateway.setUpdatedAt(Instant.now());
        txRepo.save(latestForGateway);

        healthRegistry.recordResult(gatewayName, success);
    }

    private TransactionResponse toResponse(Transaction tx) {
        TransactionResponse resp = new TransactionResponse();
        resp.setTransactionId(tx.getId());
        resp.setOrderId(tx.getOrderId());
        resp.setAmount(tx.getAmount());
        resp.setGateway(tx.getGatewayName());
        resp.setStatus(tx.getStatus().name());
        resp.setAttemptNumber(tx.getAttemptNumber());
        return resp;
    }
}
