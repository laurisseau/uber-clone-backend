package com.example.uber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Payment {
    private Long amount;
    private String clientSecret;
    private boolean paid;
}
