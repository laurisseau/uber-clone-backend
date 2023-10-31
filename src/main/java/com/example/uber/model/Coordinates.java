package com.example.uber.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class Coordinates {
    @Column(name = "latitude")
    private double lat;

    @Column(name = "longitude")
    private double lng;

    public Coordinates(){

    }

}
