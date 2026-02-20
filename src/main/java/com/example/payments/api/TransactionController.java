package com.example.payments.api;

import com.example.payments.api.dto.CallbackRequest;
import com.example.payments.api.dto.InitiateTransactionRequest;
import com.example.payments.api.dto.TransactionResponse;
import com.example.payments.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final PaymentService paymentService;

    public TransactionController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<TransactionResponse> initiate(@Valid @RequestBody InitiateTransactionRequest request) {
        return ResponseEntity.ok(paymentService.initiateTransaction(request));
    }

    @PostMapping("/callback")
    public ResponseEntity<Void> callback(@Valid @RequestBody CallbackRequest callback) {
        paymentService.handleCallback(callback);
        return ResponseEntity.ok().build();
    }
}
