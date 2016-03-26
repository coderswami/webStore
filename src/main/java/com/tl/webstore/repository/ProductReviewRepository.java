package com.tl.webstore.repository;

import com.tl.webstore.domain.ProductReview;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductReview entity.
 */
public interface ProductReviewRepository extends JpaRepository<ProductReview,Long> {

}
