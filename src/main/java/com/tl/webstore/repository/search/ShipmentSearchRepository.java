package com.tl.webstore.repository.search;

import com.tl.webstore.domain.Shipment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Shipment entity.
 */
public interface ShipmentSearchRepository extends ElasticsearchRepository<Shipment, Long> {
}
