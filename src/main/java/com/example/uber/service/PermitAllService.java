package com.example.uber.service;

import com.example.uber.config.Config;
import com.example.uber.model.Address;
import com.smartystreets.api.ClientBuilder;
import com.smartystreets.api.StaticCredentials;
import com.smartystreets.api.exceptions.SmartyException;
import com.smartystreets.api.us_autocomplete_pro.Client;
import com.smartystreets.api.us_autocomplete_pro.Lookup;
import com.smartystreets.api.us_autocomplete_pro.Suggestion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PermitAllService {

    private final Config config;
    public String getCurrentLocation(double latitude, double longitude){

        String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=" + config.getGoogleMapsApiKey();

        try {

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(apiUrl, String.class);

        } catch (Exception e) {

            return "Error occurred: " + e.getMessage();

        }
    }

    public List<Address> usAutoComplete(String destination){

        StaticCredentials credentials = new StaticCredentials(config.getSmartyStreetsAuthId(), config.getSmartyStreetsAuthToken());
        ArrayList<String> licenses = new ArrayList<>();
        licenses.add("us-autocomplete-pro-cloud");
        Client client = new ClientBuilder(credentials).withLicenses(licenses).buildUsAutocompleteProApiClient();
        Lookup lookup = new Lookup(destination);
        lookup.setMaxResults(4);
        List<Address> addresses = new ArrayList<>();

        try {
            client.send(lookup);

            // Access and print the suggestions
            for (Suggestion suggestion : lookup.getResult()) {
                Address address = new Address(suggestion.getStreetLine(), suggestion.getCity(), suggestion.getState());
                addresses.add(address);
            }

        } catch (SmartyException | IOException | InterruptedException ex) {

            return new ArrayList<>();

        }

        return addresses;
    }
}
