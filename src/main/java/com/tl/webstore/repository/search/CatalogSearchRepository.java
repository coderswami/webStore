package com.tl.webstore.repository.search;

import com.tl.webstore.domain.Catalog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Catalog entity.
 */
public interface CatalogSearchRepository extends ElasticsearchRepository<Catalog, Long> {
}
