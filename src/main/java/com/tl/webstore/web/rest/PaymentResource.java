package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.OrderHeader;
import com.tl.webstore.domain.Payment;
import com.tl.webstore.domain.enumeration.PaymentType;
import com.tl.webstore.domain.enumeration.Status;
import com.tl.webstore.repository.OrderHeaderRepository;
import com.tl.webstore.repository.PaymentRepository;
import com.tl.webstore.repository.search.PaymentSearchRepository;
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
 * REST controller for managing Payment.
 */
@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    @Inject
    private OrderHeaderRepository orderHeaderRepository;

    @Inject
    private PaymentRepository paymentRepository;

    @Inject
    private PaymentSearchRepository paymentSearchRepository;

    /**
     * POST  /payments/order/:orderId -> Create a new payment for order.
     */
    @RequestMapping(value = "/payments/order/{orderId}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment, @PathVariable Long orderId) throws URISyntaxException {
        log.debug("REST request to save Payment : {}", payment);
        log.debug("For Order : {}", orderId);
        if (payment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("payment", "idexists", "A new payment cannot already have an ID")).body(null);
        }
        if(payment.getType() != PaymentType.COD) {
            payment.setStatus(Status.CONFIRMED);
        }else {
            payment.setStatus(Status.IN_PROCESS);
        }
        Payment result = paymentRepository.save(payment);
        paymentSearchRepository.save(result);
        OrderHeader order = orderHeaderRepository.findOne(orderId);
        order.setPayment(result);
        orderHeaderRepository.save(order);
        return ResponseEntity.created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("payment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payments/order/:orderId -> Updates an existing payment.
     */
    @RequestMapping(value = "/payments/order/{orderId}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Payment> updatePayment(@Valid @RequestBody Payment payment, @PathVariable Long orderId) throws URISyntaxException {
        log.debug("REST request to update Payment : {}", payment);
        log.debug("For Order : {}", orderId);
        if (payment.getId() == null) {
            return createPayment(payment, orderId);
        }
        Payment result = paymentRepository.save(payment);
        paymentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("payment", payment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payments -> get all the payments.
     */
    @RequestMapping(value = "/payments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Payment> getAllPayments() {
        log.debug("REST request to get all Payments");
        return paymentRepository.findAll();
            }

    /**
     * GET  /payments/:id -> get the "id" payment.
     */
    @RequestMapping(value = "/payments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Payment payment = paymentRepository.findOne(id);
        return Optional.ofNullable(payment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /payments/:id -> delete the "id" payment.
     */
    @RequestMapping(value = "/payments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        log.debug("REST request to delete Payment : {}", id);
        paymentRepository.delete(id);
        paymentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("payment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/payments/:query -> search for the payment corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/payments/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Payment> searchPayments(@PathVariable String query) {
        log.debug("REST request to search Payments for query {}", query);
        return StreamSupport
            .stream(paymentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
