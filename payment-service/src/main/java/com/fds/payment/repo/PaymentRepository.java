package com.fds.payment.repo;

import com.fds.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}

