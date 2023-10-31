package com.example.uber.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UberUtils {
    public int convertToMinutes(String duration) {
        Pattern pattern = Pattern.compile("(\\d+) hour (\\d+) mins");
        Matcher matcher = pattern.matcher(duration);

        int hours = 0;
        int minutes = 0;

        if (matcher.find()) {
            hours = Integer.parseInt(matcher.group(1)); // Extract hours
            minutes = Integer.parseInt(matcher.group(2)); // Extract minutes
        }

        return hours * 60 + minutes;
    }


    public double extractMiles(String distance) {

        // Pattern to extract the floating point number
        Pattern pattern = Pattern.compile("([\\d.]+) mi");
        Matcher matcher = pattern.matcher(distance);

        double extractedNumber = 0.0;

        if (matcher.find()) {
            extractedNumber = Double.parseDouble(matcher.group(1)); // Extract the number
        }

        return extractedNumber;
    }


}
