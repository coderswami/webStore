package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.UserLogin;
import com.tl.webstore.domain.UserProfile;
import com.tl.webstore.domain.UserRole;
import com.tl.webstore.repository.UserLoginRepository;
import com.tl.webstore.repository.UserProfileRepository;
import com.tl.webstore.repository.UserRoleRepository;
import com.tl.webstore.repository.search.UserLoginSearchRepository;
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
 * REST controller for managing UserLogin.
 */
@RestController
@RequestMapping("/api")
public class UserLoginResource {

    private final Logger log = LoggerFactory.getLogger(UserLoginResource.class);
        
    @Inject
    private UserLoginRepository userLoginRepository;
    
    @Inject
    private UserLoginSearchRepository userLoginSearchRepository;
    
    @Inject
    private UserProfileRepository userProfileRepository;
    
    @Inject
    private UserRoleRepository userRoleRepository;
    
    /**
     * POST  /userLogins -> Create a new userLogin.
     */
    @RequestMapping(value = "/userLogins",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserLogin> createUserLogin(@Valid @RequestBody UserLogin userLogin) throws URISyntaxException {
        log.debug("REST request to save UserLogin : {}", userLogin);
        if (userLogin.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userLogin", "idexists", "A new userLogin cannot already have an ID")).body(null);
        }
        UserLogin result = userLoginRepository.save(userLogin);
        userLoginSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/userLogins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userLogin", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userLogins -> Updates an existing userLogin.
     */
    @RequestMapping(value = "/userLogins",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserLogin> updateUserLogin(@Valid @RequestBody UserLogin userLogin) throws URISyntaxException {
        log.debug("REST request to update UserLogin : {}", userLogin);
        if (userLogin.getId() == null) {
            return createUserLogin(userLogin);
        }
        UserLogin result = userLoginRepository.save(userLogin);
        userLoginSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userLogin", userLogin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userLogins -> get all the userLogins.
     */
    @RequestMapping(value = "/userLogins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserLogin> getAllUserLogins() {
        log.debug("REST request to get all UserLogins");
        return userLoginRepository.findAll();
            }

    /**
     * GET  /userLogins/:id -> get the "userName" userLogin.
     */
    @RequestMapping(value = "/userLogins/{username}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserLogin> getUserLogin(@PathVariable String username) {
        log.debug("REST request to get UserLoginResources : {}", username);
        UserLogin userLogin = userLoginRepository.findByUsername(username);
        if(userLogin != null)
        {
        UserProfile userProfile = userProfileRepository.findByEmail(userLogin.getUsername());
        System.out.println("geting user profile is::"+ ""+userProfile);
        UserLogin isAutthentication = userProfile.getAuthentication();
        System.out.println("user profile authentication is::"+ " "+ isAutthentication);
        if(isAutthentication != null)
        {
        	List<UserRole> ListUserRole = userRoleRepository.findByUserProfileId(userProfile.getId());
        	System.out.println("user role is::"+ " "+ ListUserRole);
        }
        }
        return Optional.ofNullable(userLogin)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userLogins/:id -> delete the "id" userLogin.
     */
    @RequestMapping(value = "/userLogins/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserLogin(@PathVariable Long id) {
        log.debug("REST request to delete UserLogin : {}", id);
        userLoginRepository.delete(id);
        userLoginSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userLogin", id.toString())).build();
    }

    /**
     * SEARCH  /_search/userLogins/:query -> search for the userLogin corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/userLogins/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserLogin> searchUserLogins(@PathVariable String query) {
        log.debug("REST request to search UserLogins for query {}", query);
        return StreamSupport
            .stream(userLoginSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
