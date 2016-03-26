package com.tl.webstore.repository;

import com.tl.webstore.domain.Shipment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Shipment entity.
 */
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {

}
