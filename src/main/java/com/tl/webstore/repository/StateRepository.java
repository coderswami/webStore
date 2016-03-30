package com.tl.webstore.repository;

import com.tl.webstore.domain.Country;
import com.tl.webstore.domain.State;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the State entity.
 */
public interface StateRepository extends JpaRepository<State,Long> {

    List<State> findByCountry(Country country);
}
