package com.tl.webstore.repository.search;

import com.tl.webstore.domain.UserLogin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the UserLogin entity.
 */
public interface UserLoginSearchRepository extends ElasticsearchRepository<UserLogin, Long> {
}
