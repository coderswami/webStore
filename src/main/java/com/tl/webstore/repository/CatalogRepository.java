package com.tl.webstore.repository;

import com.tl.webstore.domain.Catalog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Catalog entity.
 */
public interface CatalogRepository extends JpaRepository<Catalog,Long> {

}
