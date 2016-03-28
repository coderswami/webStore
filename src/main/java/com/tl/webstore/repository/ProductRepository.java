package com.tl.webstore.repository;

import com.tl.webstore.domain.Category;
import com.tl.webstore.domain.Product;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 */
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategory(Category category);
}
