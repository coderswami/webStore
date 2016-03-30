package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.OrderHeader;
import com.tl.webstore.domain.Shipment;
import com.tl.webstore.domain.enumeration.OrderType;
import com.tl.webstore.domain.enumeration.Status;
import com.tl.webstore.repository.OrderHeaderRepository;
import com.tl.webstore.repository.ShipmentRepository;
import com.tl.webstore.repository.UserProfileRepository;
import com.tl.webstore.repository.search.ShipmentSearchRepository;
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
 * REST controller for managing Shipment.
 */
@RestController
@RequestMapping("/api")
public class ShipmentResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentResource.class);

    @Inject
    private OrderHeaderRepository orderHeaderRepository;

    @Inject
    private ShipmentRepository shipmentRepository;

    @Inject
    private ShipmentSearchRepository shipmentSearchRepository;

    /**
     * POST  /shipments/order/:orderId -> Create a new shipment.
     */
    @RequestMapping(value = "/shipments/order/{orderId}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shipment> createShipment(@Valid @RequestBody Shipment shipment, @PathVariable Long orderId) throws URISyntaxException {
        log.debug("REST request to save Shipment : {}", shipment);
        log.debug("For Order : {}", orderId);
        if (shipment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shipment", "idexists", "A new shipment cannot already have an ID")).body(null);
        }
        shipment.setStatus(Status.IN_PROCESS);
        Shipment result = shipmentRepository.save(shipment);
        shipmentSearchRepository.save(result);
        OrderHeader order = orderHeaderRepository.findOne(orderId);
        order.setShipment(result);
        order.setUserProfile(result.getAddress().getUserProfile());
        orderHeaderRepository.save(order);
        return ResponseEntity.created(new URI("/api/shipments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shipment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shipments/order/:orderId -> Updates an existing shipment.
     */
    @RequestMapping(value = "/shipments/order/{orderId}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shipment> updateShipment(@Valid @RequestBody Shipment shipment, @PathVariable Long orderId) throws URISyntaxException {
        log.debug("REST request to update Shipment : {}", shipment);
        log.debug("For Order : {}", orderId);
        if (shipment.getId() == null) {
            return createShipment(shipment, orderId);
        }
        Shipment result = shipmentRepository.save(shipment);
        shipmentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shipment", shipment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shipments -> get all the shipments.
     */
    @RequestMapping(value = "/shipments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Shipment> getAllShipments() {
        log.debug("REST request to get all Shipments");
        return shipmentRepository.findAll();
            }

    /**
     * GET  /shipments/:id -> get the "id" shipment.
     */
    @RequestMapping(value = "/shipments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shipment> getShipment(@PathVariable Long id) {
        log.debug("REST request to get Shipment : {}", id);
        Shipment shipment = shipmentRepository.findOne(id);
        return Optional.ofNullable(shipment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shipments/:id -> delete the "id" shipment.
     */
    @RequestMapping(value = "/shipments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShipment(@PathVariable Long id) {
        log.debug("REST request to delete Shipment : {}", id);
        shipmentRepository.delete(id);
        shipmentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shipment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/shipments/:query -> search for the shipment corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/shipments/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Shipment> searchShipments(@PathVariable String query) {
        log.debug("REST request to search Shipments for query {}", query);
        return StreamSupport
            .stream(shipmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
