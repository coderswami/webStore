package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.UserProfile;
import com.tl.webstore.repository.UserProfileRepository;
import com.tl.webstore.repository.search.UserProfileSearchRepository;
import com.tl.webstore.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing UserProfile.
 */
@RestController
@RequestMapping("/api")
public class UserProfileResource {

    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);
        
    @Inject
    private UserProfileRepository userProfileRepository;
    
    @Inject
    private UserProfileSearchRepository userProfileSearchRepository;
    
    /**
     * POST  /userProfiles -> Create a new userProfile.
     */
    @RequestMapping(value = "/userProfiles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> createUserProfile(@Valid @RequestBody UserProfile userProfile) throws URISyntaxException {
        log.debug("REST request to save UserProfile : {}", userProfile);
        if (userProfile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userProfile", "idexists", "A new userProfile cannot already have an ID")).body(null);
        }
        UserProfile result = userProfileRepository.save(userProfile);
        userProfileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/userProfiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userProfile", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userProfiles -> Updates an existing userProfile.
     */
    @RequestMapping(value = "/userProfiles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> updateUserProfile(@Valid @RequestBody UserProfile userProfile) throws URISyntaxException {
        log.debug("REST request to update UserProfile : {}", userProfile);
        if (userProfile.getId() == null) {
            return createUserProfile(userProfile);
        }
        UserProfile result = userProfileRepository.save(userProfile);
        userProfileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userProfile", userProfile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userProfiles -> get all the userProfiles.
     */
    @RequestMapping(value = "/userProfiles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserProfile> getAllUserProfiles() {
        log.debug("REST request to get all UserProfiles");
        return userProfileRepository.findAll();
            }

    /**
     * GET  /userProfiles/:id -> get the "id" userProfile.
     */
    @RequestMapping(value = "/userProfiles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long id) {
        log.debug("REST request to get UserProfile : {}", id);
        UserProfile userProfile = userProfileRepository.findOne(id);
        return Optional.ofNullable(userProfile)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userProfiles/:id -> delete the "id" userProfile.
     */
    @RequestMapping(value = "/userProfiles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        log.debug("REST request to delete UserProfile : {}", id);
        userProfileRepository.delete(id);
        userProfileSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userProfile", id.toString())).build();
    }

    /**
     * SEARCH  /_search/userProfiles/:query -> search for the userProfile corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/userProfiles/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserProfile> searchUserProfiles(@PathVariable String query) {
        log.debug("REST request to search UserProfiles for query {}", query);
        return StreamSupport
            .stream(userProfileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
