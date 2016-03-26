package com.tl.webstore.repository.search;

import com.tl.webstore.domain.UserRole;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the UserRole entity.
 */
public interface UserRoleSearchRepository extends ElasticsearchRepository<UserRole, Long> {
}
