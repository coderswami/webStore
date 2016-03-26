package com.tl.webstore.repository.search;

import com.tl.webstore.domain.UserAddress;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the UserAddress entity.
 */
public interface UserAddressSearchRepository extends ElasticsearchRepository<UserAddress, Long> {
}
