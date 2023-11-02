package com.example.uber.repository;

import com.example.uber.model.Drivers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverAuthenticationRepository extends JpaRepository<Drivers, Integer> {
    Optional<Drivers> findByUsername(String username);
}
