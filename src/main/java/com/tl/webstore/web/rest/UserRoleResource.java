package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.UserRole;
import com.tl.webstore.repository.UserRoleRepository;
import com.tl.webstore.repository.search.UserRoleSearchRepository;
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
 * REST controller for managing UserRole.
 */
@RestController
@RequestMapping("/api")
public class UserRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);
        
    @Inject
    private UserRoleRepository userRoleRepository;
    
    @Inject
    private UserRoleSearchRepository userRoleSearchRepository;
    
    /**
     * POST  /userRoles -> Create a new userRole.
     */
    @RequestMapping(value = "/userRoles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserRole> createUserRole(@Valid @RequestBody UserRole userRole) throws URISyntaxException {
        log.debug("REST request to save UserRole : {}", userRole);
        if (userRole.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userRole", "idexists", "A new userRole cannot already have an ID")).body(null);
        }
        UserRole result = userRoleRepository.save(userRole);
        userRoleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/userRoles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userRole", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userRoles -> Updates an existing userRole.
     */
    @RequestMapping(value = "/userRoles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserRole> updateUserRole(@Valid @RequestBody UserRole userRole) throws URISyntaxException {
        log.debug("REST request to update UserRole : {}", userRole);
        if (userRole.getId() == null) {
            return createUserRole(userRole);
        }
        UserRole result = userRoleRepository.save(userRole);
        userRoleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userRole", userRole.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userRoles -> get all the userRoles.
     */
    @RequestMapping(value = "/userRoles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserRole> getAllUserRoles() {
        log.debug("REST request to get all UserRoles");
        return userRoleRepository.findAll();
            }

    /**
     * GET  /userRoles/:id -> get the "id" userRole.
     */
    @RequestMapping(value = "/userRoles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserRole> getUserRole(@PathVariable Long id) {
        log.debug("REST request to get UserRole : {}", id);
        UserRole userRole = userRoleRepository.findOne(id);
        return Optional.ofNullable(userRole)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userRoles/:id -> delete the "id" userRole.
     */
    @RequestMapping(value = "/userRoles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        log.debug("REST request to delete UserRole : {}", id);
        userRoleRepository.delete(id);
        userRoleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userRole", id.toString())).build();
    }

    /**
     * SEARCH  /_search/userRoles/:query -> search for the userRole corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/userRoles/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserRole> searchUserRoles(@PathVariable String query) {
        log.debug("REST request to search UserRoles for query {}", query);
        return StreamSupport
            .stream(userRoleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
