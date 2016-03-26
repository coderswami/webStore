package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.State;
import com.tl.webstore.repository.StateRepository;
import com.tl.webstore.repository.search.StateSearchRepository;
import com.tl.webstore.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing State.
 */
@RestController
@RequestMapping("/api")
public class StateResource {

    private final Logger log = LoggerFactory.getLogger(StateResource.class);
        
    @Inject
    private StateRepository stateRepository;
    
    @Inject
    private StateSearchRepository stateSearchRepository;
    
    /**
     * POST  /states -> Create a new state.
     */
    @RequestMapping(value = "/states",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<State> createState(@RequestBody State state) throws URISyntaxException {
        log.debug("REST request to save State : {}", state);
        if (state.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("state", "idexists", "A new state cannot already have an ID")).body(null);
        }
        State result = stateRepository.save(state);
        stateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("state", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /states -> Updates an existing state.
     */
    @RequestMapping(value = "/states",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<State> updateState(@RequestBody State state) throws URISyntaxException {
        log.debug("REST request to update State : {}", state);
        if (state.getId() == null) {
            return createState(state);
        }
        State result = stateRepository.save(state);
        stateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("state", state.getId().toString()))
            .body(result);
    }

    /**
     * GET  /states -> get all the states.
     */
    @RequestMapping(value = "/states",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<State> getAllStates() {
        log.debug("REST request to get all States");
        return stateRepository.findAll();
            }

    /**
     * GET  /states/:id -> get the "id" state.
     */
    @RequestMapping(value = "/states/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<State> getState(@PathVariable Long id) {
        log.debug("REST request to get State : {}", id);
        State state = stateRepository.findOne(id);
        return Optional.ofNullable(state)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /states/:id -> delete the "id" state.
     */
    @RequestMapping(value = "/states/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteState(@PathVariable Long id) {
        log.debug("REST request to delete State : {}", id);
        stateRepository.delete(id);
        stateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("state", id.toString())).build();
    }

    /**
     * SEARCH  /_search/states/:query -> search for the state corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/states/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<State> searchStates(@PathVariable String query) {
        log.debug("REST request to search States for query {}", query);
        return StreamSupport
            .stream(stateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
