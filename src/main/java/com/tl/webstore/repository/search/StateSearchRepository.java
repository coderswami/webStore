package com.tl.webstore.repository.search;

import com.tl.webstore.domain.State;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the State entity.
 */
public interface StateSearchRepository extends ElasticsearchRepository<State, Long> {
}
