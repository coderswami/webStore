package com.tl.webstore.repository.search;

import com.tl.webstore.domain.ProductAttr;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProductAttr entity.
 */
public interface ProductAttrSearchRepository extends ElasticsearchRepository<ProductAttr, Long> {
}
