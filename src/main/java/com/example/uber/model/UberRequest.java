package com.example.uber.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class UberRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String username;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "origin_latitude")),
            @AttributeOverride(name = "lng", column = @Column(name = "origin_longitude"))
    })
    private Coordinates origin;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "destination_latitude")),
            @AttributeOverride(name = "lng", column = @Column(name = "destination_longitude"))
    })
    private Coordinates destination;

    private String number;

    private String duration;

    private String distance;

    private int cost;

    private boolean paid;

    public UberRequest(){

    }
}
