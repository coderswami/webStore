package com.tl.webstore.repository;

import com.tl.webstore.domain.ProductPrice;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductPrice entity.
 */
public interface ProductPriceRepository extends JpaRepository<ProductPrice,Long> {

}
