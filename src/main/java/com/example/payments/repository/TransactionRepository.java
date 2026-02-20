package com.example.payments.repository;

import com.example.payments.domain.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {

    private final Map<String, Transaction> byId = new ConcurrentHashMap<>();

    public Transaction save(Transaction tx) {
        byId.put(tx.getId(), tx);
        return tx;
    }

    public List<Transaction> findByOrderId(String orderId) {
        return byId.values().stream()
                .filter(t -> t.getOrderId().equals(orderId))
                .sorted(Comparator.comparing(Transaction::getCreatedAt))
                .collect(Collectors.toList());
    }
}
