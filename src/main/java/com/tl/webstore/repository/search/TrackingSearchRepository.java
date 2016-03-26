package com.tl.webstore.repository.search;

import com.tl.webstore.domain.Tracking;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Tracking entity.
 */
public interface TrackingSearchRepository extends ElasticsearchRepository<Tracking, Long> {
}
