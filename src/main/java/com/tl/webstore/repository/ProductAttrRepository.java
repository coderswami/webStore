package com.tl.webstore.repository;

import com.tl.webstore.domain.ProductAttr;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductAttr entity.
 */
public interface ProductAttrRepository extends JpaRepository<ProductAttr,Long> {

}
