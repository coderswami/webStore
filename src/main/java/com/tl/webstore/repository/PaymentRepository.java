package com.tl.webstore.repository;

import com.tl.webstore.domain.Payment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Payment entity.
 */
public interface PaymentRepository extends JpaRepository<Payment,Long> {

}
