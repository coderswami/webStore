package com.tl.webstore.repository;

import com.tl.webstore.domain.Catalog;

import com.tl.webstore.domain.Country;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Catalog entity.
 */
public interface CatalogRepository extends JpaRepository<Catalog,Long> {

    Catalog findByActiveAndCountry(Boolean active, Country country);

}
