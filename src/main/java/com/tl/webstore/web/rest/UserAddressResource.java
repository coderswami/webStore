package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.UserAddress;
import com.tl.webstore.repository.UserAddressRepository;
import com.tl.webstore.repository.search.UserAddressSearchRepository;
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
 * REST controller for managing UserAddress.
 */
@RestController
@RequestMapping("/api")
public class UserAddressResource {

    private final Logger log = LoggerFactory.getLogger(UserAddressResource.class);
        
    @Inject
    private UserAddressRepository userAddressRepository;
    
    @Inject
    private UserAddressSearchRepository userAddressSearchRepository;
    
    /**
     * POST  /userAddresss -> Create a new userAddress.
     */
    @RequestMapping(value = "/userAddresss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserAddress> createUserAddress(@Valid @RequestBody UserAddress userAddress) throws URISyntaxException {
        log.debug("REST request to save UserAddress : {}", userAddress);
        if (userAddress.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userAddress", "idexists", "A new userAddress cannot already have an ID")).body(null);
        }
        UserAddress result = userAddressRepository.save(userAddress);
        userAddressSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/userAddresss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userAddress", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userAddresss -> Updates an existing userAddress.
     */
    @RequestMapping(value = "/userAddresss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserAddress> updateUserAddress(@Valid @RequestBody UserAddress userAddress) throws URISyntaxException {
        log.debug("REST request to update UserAddress : {}", userAddress);
        if (userAddress.getId() == null) {
            return createUserAddress(userAddress);
        }
        UserAddress result = userAddressRepository.save(userAddress);
        userAddressSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userAddress", userAddress.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userAddresss -> get all the userAddresss.
     */
    @RequestMapping(value = "/userAddresss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserAddress> getAllUserAddresss() {
        log.debug("REST request to get all UserAddresss");
        return userAddressRepository.findAll();
            }

    /**
     * GET  /userAddresss/:id -> get the "id" userAddress.
     */
    @RequestMapping(value = "/userAddresss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserAddress> getUserAddress(@PathVariable Long id) {
        log.debug("REST request to get UserAddress : {}", id);
        UserAddress userAddress = userAddressRepository.findOne(id);
        return Optional.ofNullable(userAddress)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userAddresss/:id -> delete the "id" userAddress.
     */
    @RequestMapping(value = "/userAddresss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserAddress(@PathVariable Long id) {
        log.debug("REST request to delete UserAddress : {}", id);
        userAddressRepository.delete(id);
        userAddressSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userAddress", id.toString())).build();
    }

    /**
     * SEARCH  /_search/userAddresss/:query -> search for the userAddress corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/userAddresss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserAddress> searchUserAddresss(@PathVariable String query) {
        log.debug("REST request to search UserAddresss for query {}", query);
        return StreamSupport
            .stream(userAddressSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
