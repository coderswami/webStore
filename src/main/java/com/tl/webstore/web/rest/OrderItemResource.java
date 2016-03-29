package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.OrderHeader;
import com.tl.webstore.domain.OrderItem;
import com.tl.webstore.domain.enumeration.OrderType;
import com.tl.webstore.repository.OrderHeaderRepository;
import com.tl.webstore.repository.OrderItemRepository;
import com.tl.webstore.repository.search.OrderHeaderSearchRepository;
import com.tl.webstore.repository.search.OrderItemSearchRepository;
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
 * REST controller for managing OrderItem.
 */
@RestController
@RequestMapping("/api")
public class OrderItemResource {

    private final Logger log = LoggerFactory.getLogger(OrderItemResource.class);

    @Inject
    private OrderHeaderRepository orderHeaderRepository;

    @Inject
    private OrderHeaderSearchRepository orderHeaderSearchRepository;

    @Inject
    private OrderItemRepository orderItemRepository;

    @Inject
    private OrderItemSearchRepository orderItemSearchRepository;

    /**
     * POST  /orderItems -> Create a new orderItem.
     */
    @RequestMapping(value = "/orderItems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderItem> createOrderItem(@Valid @RequestBody OrderItem orderItem) throws URISyntaxException {
        log.debug("REST request to save OrderItem : {}", orderItem);
        if (orderItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderItem", "idexists", "A new orderItem cannot already have an ID")).body(null);
        }
        OrderItem result = orderItemRepository.save(orderItem);
        orderItemSearchRepository.save(result);
        OrderHeader orderHeader = result.getOrderHeader();
        List<OrderItem> items = orderItemRepository.findByOrderHeader(orderHeader);
        Double orderTotal = 0.0;
        for (int i = 0; i < items.size(); i++) {
            orderTotal += items.get(i).getPrice();
        }
        orderHeader.setOrderTotal(orderTotal);
        orderHeader = orderHeaderRepository.save(orderHeader);
        orderHeaderSearchRepository.save(orderHeader);
        return ResponseEntity.created(new URI("/api/orderItems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orderItems -> Updates an existing orderItem.
     */
    @RequestMapping(value = "/orderItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderItem> updateOrderItem(@Valid @RequestBody OrderItem orderItem) throws URISyntaxException {
        log.debug("REST request to update OrderItem : {}", orderItem);
        if (orderItem.getId() == null) {
            return createOrderItem(orderItem);
        }
        OrderItem result = orderItemRepository.save(orderItem);
        orderItemSearchRepository.save(result);
        OrderHeader orderHeader = result.getOrderHeader();
        List<OrderItem> items = orderItemRepository.findByOrderHeader(orderHeader);
        Double orderTotal = 0.0;
        for (int i = 0; i < items.size(); i++) {
            orderTotal += items.get(i).getPrice();
        }
        orderHeader.setOrderTotal(orderTotal);
        orderHeader = orderHeaderRepository.save(orderHeader);
        orderHeaderSearchRepository.save(orderHeader);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderItem", orderItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orderItems -> get all the orderItems.
     */
    @RequestMapping(value = "/orderItems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OrderItem> getAllOrderItems() {
        log.debug("REST request to get all OrderItems");
        return orderItemRepository.findAll();
    }

    /**
     * GET  /orderItems/orderHeader/:orderHeaderId -> get all the orderItems.
     */
    @RequestMapping(value = "/orderItems/orderHeader/{orderHeaderId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OrderItem> getOrderItemsByOrderHeader(@PathVariable Long orderHeaderId) {
        log.debug("REST request to get all OrderItems", orderHeaderId);
        return orderItemRepository.findByOrderHeader(orderHeaderRepository.findOne(orderHeaderId));
    }

    /**
     * GET  /orderItems/:id -> get the "id" orderItem.
     */
    @RequestMapping(value = "/orderItems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable Long id) {
        log.debug("REST request to get OrderItem : {}", id);
        OrderItem orderItem = orderItemRepository.findOne(id);
        return Optional.ofNullable(orderItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /orderItems/:id -> delete the "id" orderItem.
     */
    @RequestMapping(value = "/orderItems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete OrderItem : {}", id);
        OrderHeader orderHeader = orderItemRepository.findOne(id).getOrderHeader();
        orderItemRepository.delete(id);
        orderItemSearchRepository.delete(id);
        List<OrderItem> items = orderItemRepository.findByOrderHeader(orderHeader);
        if(items.size() > 0) {
            Double orderTotal = 0.0;
            for (int i = 0; i < items.size(); i++) {
                orderTotal += items.get(i).getPrice();
            }
            orderHeader.setOrderTotal(orderTotal);
            orderHeaderRepository.save(orderHeader);
        }else {
            orderHeaderRepository.delete(orderHeader.getId());
            orderHeaderSearchRepository.delete(orderHeader.getId());
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderItem", id.toString())).build();
    }

    /**
     * SEARCH  /_search/orderItems/:query -> search for the orderItem corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/orderItems/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OrderItem> searchOrderItems(@PathVariable String query) {
        log.debug("REST request to search OrderItems for query {}", query);
        return StreamSupport
            .stream(orderItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
