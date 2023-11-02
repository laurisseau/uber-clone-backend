package com.example.uber.controller;

import com.example.uber.model.Drivers;
import com.example.uber.service.DriverAuthenticationService;
import com.example.uber.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/driver")
@RequiredArgsConstructor
public class DriverAuthenticationController {

    private final DriverAuthenticationService driverAuthenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    @PostMapping("/login")
    ResponseEntity<Map<String, Object>> login(@RequestBody Drivers driver){
        try {

            UserDetails driverDetails = driverAuthenticationService.loadUserByUsername(driver.getUsername());

           Map<String, Object> driverInfo = driverAuthenticationService.driverInfo(driver.getUsername(), jwtUtils.generateToken(driverDetails));

            authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(driver.getUsername(), driver.getPassword())
            );

            return ResponseEntity.ok(driverInfo);

        }catch(Exception e){
            Map<String, Object> driverInfo = new HashMap<>();
            driverInfo.put("Login failed", e.getMessage());
            return ResponseEntity.badRequest().body(driverInfo);
        }
    }

    @PostMapping("/signup")
    ResponseEntity<String> Signup(@RequestBody Drivers driver){
        try{

            driverAuthenticationService.signUp(driver);

            return ResponseEntity.ok("Driver created");

        }catch(Exception e){
            return ResponseEntity.badRequest().body("Signup failed: " + e.getMessage());
        }
    }
}
