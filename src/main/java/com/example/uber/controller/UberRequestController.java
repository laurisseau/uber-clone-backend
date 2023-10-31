package com.example.uber.controller;

import com.example.uber.model.UberRequest;
import com.example.uber.repository.UberRequestRepository;
import com.example.uber.service.UberRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UberRequestController {

    private final UberRequestRepository uberRequestRepository;
    private final UberRequestService uberRequestService;
    @GetMapping("/getDirections/origin={origin}&dest={destination}")
    public String getDirections(@PathVariable String destination, @PathVariable String origin) {
        return uberRequestService.getDirections(origin, destination);
    }

    @PostMapping("/uberRequest")
    public ResponseEntity<UberRequest> createUberRequest(@RequestBody UberRequest uberRequest) {
        return uberRequestService.createUberRequest(uberRequest);
    }

    @GetMapping("/uberRequest/{uberRequestId}")
    public Optional<UberRequest> getUberRequest(@PathVariable int uberRequestId) {
        return uberRequestRepository.findById(uberRequestId);
    }




}
