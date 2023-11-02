package com.example.uber.service;

import com.example.uber.config.Config;
import com.example.uber.model.Payment;
import com.example.uber.model.UberRequest;
import com.example.uber.repository.UberRequestRepository;
import com.example.uber.utils.UberUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class UberRequestService {

    private final Config config;
    private final UberUtils uberUtils;
    private final UberRequestRepository uberRequestRepository;

    public String getDirections(String origin, String destination) {

        String modifiedOrigin = origin.replace(" ", "+");
        String modifiedDestination = destination.replace(" ", "+");

        String apiUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + modifiedOrigin +
                "&destination=" + modifiedDestination + "&key=" + config.getGoogleMapsApiKey();

        RestTemplate restTemplate = new RestTemplate();

        try {

            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> restTemplate.getForObject(apiUrl, String.class));
            return future.get();

        } catch (Exception e) {

            return "Error occurred: " + e.getMessage();

        }
    }

    public ResponseEntity<UberRequest> createUberRequest(UberRequest uberRequest){
        try{
            String duration = uberRequest.getDuration();
            String distance = uberRequest.getDistance();

            Long amount = (long) ((uberUtils.convertToMinutes(duration) * .25 + uberUtils.extractMiles(distance)) * 100);

            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setPaid(false);

            uberRequest.setPayment(payment);

            uberRequestRepository.save(uberRequest);

            return ResponseEntity.ok(uberRequest);

        }catch(Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().body(uberRequest);

        }
    }

}
