package com.example.uber.repository;

import com.example.uber.model.UberRequest;
import com.example.uber.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UberRequestRepository extends JpaRepository<UberRequest, Integer> {
    List<UberRequest> findAllByPaymentPaidAndAccepted(boolean paid, boolean accepted);

}
