package com.example.uber.controller;

import com.example.uber.model.Address;
import com.example.uber.service.PermitAllService;
import com.smartystreets.api.ClientBuilder;
import com.smartystreets.api.StaticCredentials;
import com.smartystreets.api.exceptions.SmartyException;
import com.smartystreets.api.us_autocomplete_pro.Client;
import com.smartystreets.api.us_autocomplete_pro.Lookup;
import com.smartystreets.api.us_autocomplete_pro.Suggestion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/permitAll")
@RequiredArgsConstructor
public class PermitAllController {

    private final PermitAllService permitAllService;

    @GetMapping("/getCurrentLocation/lat={latitude}&lng={longitude}")
    public String getCurrentLocation(@PathVariable double latitude, @PathVariable double longitude) {
        return permitAllService.getCurrentLocation(latitude, longitude);
    }
    @GetMapping("/Autocomplete/dest={destination}")
    public List<Address> usAutocomplete(@PathVariable String destination) {
        return permitAllService.usAutoComplete(destination);
    }
}
