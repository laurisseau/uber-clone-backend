package com.example.uber.controller;

import com.example.uber.model.Payment;
import com.example.uber.repository.UberRequestRepository;
import com.example.uber.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final UberRequestRepository uberRequestRepository;
    private final PaymentService paymentService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/create-payment-intent")
    public Payment payment(@RequestBody Payment payment) throws StripeException {
        return paymentService.payment(payment.getAmount());
    }

    @GetMapping("/paidUberRequest/{uberRequestId}")
    public ResponseEntity<Optional<Object>> paidUberRequest(@PathVariable int uberRequestId) {
        try {
            Optional<Object> paidUberRequest = uberRequestRepository.findById(uberRequestId)
                    .map(uberRequest -> {
                        uberRequest.getPayment().setPaid(true);
                        return uberRequestRepository.save(uberRequest);
                    });
            return ResponseEntity.ok(paidUberRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //return ResponseEntity.badRequest().body(uberRequest);
            return null;
        }

    }
}
