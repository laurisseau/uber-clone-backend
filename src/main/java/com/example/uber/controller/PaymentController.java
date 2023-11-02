package com.example.uber.controller;

import com.example.uber.model.Payment;
import com.example.uber.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public Payment payment(@RequestBody Payment payment) throws StripeException {
        return paymentService.payment(payment.getAmount());
    }
}
