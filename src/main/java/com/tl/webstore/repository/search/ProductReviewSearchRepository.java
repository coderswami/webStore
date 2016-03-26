package com.tl.webstore.repository.search;

import com.tl.webstore.domain.ProductReview;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProductReview entity.
 */
public interface ProductReviewSearchRepository extends ElasticsearchRepository<ProductReview, Long> {
}
