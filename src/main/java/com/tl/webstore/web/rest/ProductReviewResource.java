package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.ProductReview;
import com.tl.webstore.repository.ProductReviewRepository;
import com.tl.webstore.repository.search.ProductReviewSearchRepository;
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
 * REST controller for managing ProductReview.
 */
@RestController
@RequestMapping("/api")
public class ProductReviewResource {

    private final Logger log = LoggerFactory.getLogger(ProductReviewResource.class);
        
    @Inject
    private ProductReviewRepository productReviewRepository;
    
    @Inject
    private ProductReviewSearchRepository productReviewSearchRepository;
    
    /**
     * POST  /productReviews -> Create a new productReview.
     */
    @RequestMapping(value = "/productReviews",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductReview> createProductReview(@Valid @RequestBody ProductReview productReview) throws URISyntaxException {
        log.debug("REST request to save ProductReview : {}", productReview);
        if (productReview.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productReview", "idexists", "A new productReview cannot already have an ID")).body(null);
        }
        ProductReview result = productReviewRepository.save(productReview);
        productReviewSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/productReviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productReview", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /productReviews -> Updates an existing productReview.
     */
    @RequestMapping(value = "/productReviews",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductReview> updateProductReview(@Valid @RequestBody ProductReview productReview) throws URISyntaxException {
        log.debug("REST request to update ProductReview : {}", productReview);
        if (productReview.getId() == null) {
            return createProductReview(productReview);
        }
        ProductReview result = productReviewRepository.save(productReview);
        productReviewSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productReview", productReview.getId().toString()))
            .body(result);
    }

    /**
     * GET  /productReviews -> get all the productReviews.
     */
    @RequestMapping(value = "/productReviews",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductReview> getAllProductReviews() {
        log.debug("REST request to get all ProductReviews");
        return productReviewRepository.findAll();
            }

    /**
     * GET  /productReviews/:id -> get the "id" productReview.
     */
    @RequestMapping(value = "/productReviews/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductReview> getProductReview(@PathVariable Long id) {
        log.debug("REST request to get ProductReview : {}", id);
        ProductReview productReview = productReviewRepository.findOne(id);
        return Optional.ofNullable(productReview)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /productReviews/:id -> delete the "id" productReview.
     */
    @RequestMapping(value = "/productReviews/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductReview(@PathVariable Long id) {
        log.debug("REST request to delete ProductReview : {}", id);
        productReviewRepository.delete(id);
        productReviewSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productReview", id.toString())).build();
    }

    /**
     * SEARCH  /_search/productReviews/:query -> search for the productReview corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/productReviews/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductReview> searchProductReviews(@PathVariable String query) {
        log.debug("REST request to search ProductReviews for query {}", query);
        return StreamSupport
            .stream(productReviewSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
