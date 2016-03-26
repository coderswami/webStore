package com.tl.webstore.repository.search;

import com.tl.webstore.domain.ProductPrice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProductPrice entity.
 */
public interface ProductPriceSearchRepository extends ElasticsearchRepository<ProductPrice, Long> {
}
