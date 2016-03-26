package com.tl.webstore.repository;

import com.tl.webstore.domain.Currency;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Currency entity.
 */
public interface CurrencyRepository extends JpaRepository<Currency,Long> {

}
