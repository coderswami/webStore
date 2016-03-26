package com.tl.webstore.repository.search;

import com.tl.webstore.domain.OrderHeader;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OrderHeader entity.
 */
public interface OrderHeaderSearchRepository extends ElasticsearchRepository<OrderHeader, Long> {
}
