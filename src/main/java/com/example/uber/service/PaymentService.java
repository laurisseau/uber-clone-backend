package com.example.uber.service;

import com.example.uber.config.Config;
import com.example.uber.model.Payment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PaymentService {

    private final Config config;

    public Payment payment(Long amount) throws StripeException {

        Stripe.apiKey = config.getStripeSecretApiKey();

        PaymentIntent paymentIntent = PaymentIntent.create(
                Map.of(
                        "amount", amount,
                        "currency", "usd",
                        "payment_method_types", List.of("card")
                )
        );

        Payment payment = new Payment();

        payment.setClientSecret(paymentIntent.getClientSecret());
        payment.setAmount(amount);


        return payment;

    }
}
