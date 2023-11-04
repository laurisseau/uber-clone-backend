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

    private String originAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "destination_latitude")),
            @AttributeOverride(name = "lng", column = @Column(name = "destination_longitude"))
    })
    private Coordinates destination;

    private String destinationAddress;

    private String number;

    private String duration;

    private String distance;

    private boolean accepted;

    private boolean driverArrived;

    private boolean dropOffCompleted;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount")),
            @AttributeOverride(name = "client_secret", column = @Column(name = "client_secret")),
            @AttributeOverride(name = "paid", column = @Column(name = "paid"))

    })
    private Payment payment;

    // many UberRequests can have the same driver
    @JoinColumn(name = "driver_id")
    @ManyToOne
    private Drivers driver;

    public UberRequest(){

    }
}
