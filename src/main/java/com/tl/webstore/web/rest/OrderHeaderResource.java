package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.OrderHeader;
import com.tl.webstore.domain.enumeration.OrderType;
import com.tl.webstore.repository.OrderHeaderRepository;
import com.tl.webstore.repository.search.OrderHeaderSearchRepository;
import com.tl.webstore.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing OrderHeader.
 */
@RestController
@RequestMapping("/api")
public class OrderHeaderResource {

    private final Logger log = LoggerFactory.getLogger(OrderHeaderResource.class);

    @Inject
    private OrderHeaderRepository orderHeaderRepository;

    @Inject
    private OrderHeaderSearchRepository orderHeaderSearchRepository;

    /**
     * POST  /orderHeaders -> Create a new orderHeader.
     */
    @RequestMapping(value = "/orderHeaders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderHeader> createOrderHeader(@Valid @RequestBody OrderHeader orderHeader, HttpServletRequest request) throws URISyntaxException {
        System.out.println(request.getRemoteUser());
        System.out.println(request.getRemoteAddr());
        System.out.println(request.getRemoteHost());
        System.out.println(request.getRemotePort());
        log.debug("REST request to save OrderHeader : {}", orderHeader);
        if (orderHeader.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderHeader", "idexists", "A new orderHeader cannot already have an ID")).body(null);
        }
        OrderHeader result = orderHeaderRepository.save(orderHeader);
        orderHeaderSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/orderHeaders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderHeader", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orderHeaders -> Updates an existing orderHeader.
     */
    @RequestMapping(value = "/orderHeaders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderHeader> updateOrderHeader(@Valid @RequestBody OrderHeader orderHeader, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update OrderHeader : {}", orderHeader);
        if (orderHeader.getId() == null) {
            return createOrderHeader(orderHeader, request);
        }
        OrderHeader result = orderHeaderRepository.save(orderHeader);
        orderHeaderSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderHeader", orderHeader.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orderHeaders -> get all the orderHeaders.
     */
    @RequestMapping(value = "/orderHeaders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OrderHeader> getAllOrderHeaders() {
        log.debug("REST request to get all OrderHeaders");
        return orderHeaderRepository.findAll();
            }

    /**
     * GET  /orderHeaders/type/:orderType -> get all the orderHeaders.
     */
    @RequestMapping(value = "/orderHeaders/type/{orderType}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OrderHeader> getOrderHeadersByOrderType(@PathVariable String orderType) {
        log.debug("REST request to get OrderHeaders : {}", orderType);
        return orderHeaderRepository.findByType(OrderType.valueOf(orderType));
    }

    /**
     * GET  /orderHeaders/:id -> get the "id" orderHeader.
     */
    @RequestMapping(value = "/orderHeaders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderHeader> getOrderHeader(@PathVariable Long id) {
        log.debug("REST request to get OrderHeader : {}", id);
        OrderHeader orderHeader = orderHeaderRepository.findOne(id);
        return Optional.ofNullable(orderHeader)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /orderHeaders/:id -> delete the "id" orderHeader.
     */
    @RequestMapping(value = "/orderHeaders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderHeader(@PathVariable Long id) {
        log.debug("REST request to delete OrderHeader : {}", id);
        orderHeaderRepository.delete(id);
        orderHeaderSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderHeader", id.toString())).build();
    }

    /**
     * SEARCH  /_search/orderHeaders/:query -> search for the orderHeader corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/orderHeaders/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OrderHeader> searchOrderHeaders(@PathVariable String query) {
        log.debug("REST request to search OrderHeaders for query {}", query);
        return StreamSupport
            .stream(orderHeaderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
