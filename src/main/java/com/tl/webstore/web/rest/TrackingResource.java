package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.Tracking;
import com.tl.webstore.repository.TrackingRepository;
import com.tl.webstore.repository.search.TrackingSearchRepository;
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
 * REST controller for managing Tracking.
 */
@RestController
@RequestMapping("/api")
public class TrackingResource {

    private final Logger log = LoggerFactory.getLogger(TrackingResource.class);
        
    @Inject
    private TrackingRepository trackingRepository;
    
    @Inject
    private TrackingSearchRepository trackingSearchRepository;
    
    /**
     * POST  /trackings -> Create a new tracking.
     */
    @RequestMapping(value = "/trackings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tracking> createTracking(@Valid @RequestBody Tracking tracking) throws URISyntaxException {
        log.debug("REST request to save Tracking : {}", tracking);
        if (tracking.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tracking", "idexists", "A new tracking cannot already have an ID")).body(null);
        }
        Tracking result = trackingRepository.save(tracking);
        trackingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/trackings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tracking", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trackings -> Updates an existing tracking.
     */
    @RequestMapping(value = "/trackings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tracking> updateTracking(@Valid @RequestBody Tracking tracking) throws URISyntaxException {
        log.debug("REST request to update Tracking : {}", tracking);
        if (tracking.getId() == null) {
            return createTracking(tracking);
        }
        Tracking result = trackingRepository.save(tracking);
        trackingSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tracking", tracking.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trackings -> get all the trackings.
     */
    @RequestMapping(value = "/trackings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tracking> getAllTrackings() {
        log.debug("REST request to get all Trackings");
        return trackingRepository.findAll();
            }

    /**
     * GET  /trackings/:id -> get the "id" tracking.
     */
    @RequestMapping(value = "/trackings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tracking> getTracking(@PathVariable Long id) {
        log.debug("REST request to get Tracking : {}", id);
        Tracking tracking = trackingRepository.findOne(id);
        return Optional.ofNullable(tracking)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trackings/:id -> delete the "id" tracking.
     */
    @RequestMapping(value = "/trackings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTracking(@PathVariable Long id) {
        log.debug("REST request to delete Tracking : {}", id);
        trackingRepository.delete(id);
        trackingSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tracking", id.toString())).build();
    }

    /**
     * SEARCH  /_search/trackings/:query -> search for the tracking corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/trackings/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tracking> searchTrackings(@PathVariable String query) {
        log.debug("REST request to search Trackings for query {}", query);
        return StreamSupport
            .stream(trackingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
