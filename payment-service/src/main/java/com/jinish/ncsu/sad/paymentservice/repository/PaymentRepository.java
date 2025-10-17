package com.jinish.ncsu.sad.paymentservice.repository;

import com.jinish.ncsu.sad.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {}