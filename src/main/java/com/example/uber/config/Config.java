package com.example.uber.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${google.maps.apiKey}")
    private String googleMapsApiKey;

    @Value("${smartyStreets.authId}")
    private String smartyStreetsAuthId;

    @Value("${smartyStreets.authToken}")
    private String smartyStreetsAuthToken;

    @Value("${stripe.secret.apiKey}")
    private String stripeSecretApiKey;

    @Bean
    public String getJwtSecret() {return jwtSecret;}

    @Bean
    public String getGoogleMapsApiKey() {return googleMapsApiKey;}

    @Bean
    public String getSmartyStreetsAuthId() {return smartyStreetsAuthId;}
    @Bean
    public String getSmartyStreetsAuthToken() {return smartyStreetsAuthToken;}

    @Bean
    public String getStripeSecretApiKey() {return stripeSecretApiKey;}

}
