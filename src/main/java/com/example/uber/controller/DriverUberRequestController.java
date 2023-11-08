package com.example.uber.controller;

import com.example.uber.model.Drivers;
import com.example.uber.model.Payment;
import com.example.uber.model.UberRequest;
import com.example.uber.repository.DriverAuthenticationRepository;
import com.example.uber.repository.UberRequestRepository;
import com.example.uber.service.DriverUberRequestService;
import com.example.uber.service.UberRequestService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Driver;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/driver")
public class DriverUberRequestController {
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;
    private final DriverUberRequestService driverUberRequestService;
    private final UberRequestService uberRequestService;
    private final UberRequestRepository uberRequestRepository;
    private final DriverAuthenticationRepository driverAuthenticationRepository;
    @GetMapping("/uberRequest")
    public List<UberRequest> getAllUberRequest() {
        return uberRequestRepository.findAllByPaymentPaidAndAccepted(true, false);
    }

    @PutMapping("/uberRequest/{uberRequestId}")
    public Optional<UberRequest> acceptUberRequest(@PathVariable int uberRequestId, @RequestBody Drivers driverInfo) {

        Drivers existingDriver = driverAuthenticationRepository.findByUsername(driverInfo.getUsername()).orElse(null);
        Optional<UberRequest> uberRequestOptional = uberRequestRepository.findById(uberRequestId);

        if(uberRequestOptional.isPresent()) {
            UberRequest uberRequest = uberRequestOptional.get();
            uberRequest.setAccepted(true);
            uberRequest.setDriver(existingDriver);
            Optional<UberRequest> update = Optional.of(uberRequestRepository.save(uberRequest));
            //messagingTemplate.convertAndSend("/driver/acceptUberRequest", update);
            return update;
        }

        return Optional.empty();

    }

    @GetMapping("/getDirections/origin={origin}&dest={destination}")
    public String getDirections(@PathVariable String destination, @PathVariable String origin) {
        return uberRequestService.getDirections(origin, destination);
    }


}
