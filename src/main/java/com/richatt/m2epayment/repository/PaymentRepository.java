package com.richatt.m2epayment.repository;

import com.richatt.m2epayment.domain.Payment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {}
