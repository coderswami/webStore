package com.tl.webstore.repository.search;

import com.tl.webstore.domain.Currency;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Currency entity.
 */
public interface CurrencySearchRepository extends ElasticsearchRepository<Currency, Long> {
}
