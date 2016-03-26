package com.tl.webstore.repository.search;

import com.tl.webstore.domain.UserProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the UserProfile entity.
 */
public interface UserProfileSearchRepository extends ElasticsearchRepository<UserProfile, Long> {
}
