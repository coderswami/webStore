package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.ProductAttr;
import com.tl.webstore.repository.ProductAttrRepository;
import com.tl.webstore.repository.ProductRepository;
import com.tl.webstore.repository.search.ProductAttrSearchRepository;
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
 * REST controller for managing ProductAttr.
 */
@RestController
@RequestMapping("/api")
public class ProductAttrResource {

    private final Logger log = LoggerFactory.getLogger(ProductAttrResource.class);

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductAttrRepository productAttrRepository;

    @Inject
    private ProductAttrSearchRepository productAttrSearchRepository;

    /**
     * POST  /productAttrs -> Create a new productAttr.
     */
    @RequestMapping(value = "/productAttrs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductAttr> createProductAttr(@Valid @RequestBody ProductAttr productAttr) throws URISyntaxException {
        log.debug("REST request to save ProductAttr : {}", productAttr);
        if (productAttr.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productAttr", "idexists", "A new productAttr cannot already have an ID")).body(null);
        }
        ProductAttr result = productAttrRepository.save(productAttr);
        productAttrSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/productAttrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productAttr", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /productAttrs -> Updates an existing productAttr.
     */
    @RequestMapping(value = "/productAttrs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductAttr> updateProductAttr(@Valid @RequestBody ProductAttr productAttr) throws URISyntaxException {
        log.debug("REST request to update ProductAttr : {}", productAttr);
        if (productAttr.getId() == null) {
            return createProductAttr(productAttr);
        }
        ProductAttr result = productAttrRepository.save(productAttr);
        productAttrSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productAttr", productAttr.getId().toString()))
            .body(result);
    }

    /**
     * GET  /productAttrs -> get all the productAttrs.
     */
    @RequestMapping(value = "/productAttrs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductAttr> getAllProductAttrs() {
        log.debug("REST request to get all ProductAttrs");
        return productAttrRepository.findAll();
    }

    /**
     * GET  /productAttrs/product/:productId -> get all the productAttrs.
     */
    @RequestMapping(value = "/productAttrs/product/{productId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductAttr> getProductAttrsByProduct(@PathVariable Long productId) {
        log.debug("REST request to get all ProductAttrs : {}", productId);
        return productAttrRepository.findByProduct(productRepository.findOne(productId));
    }

    /**
     * GET  /productAttrs/:id -> get the "id" productAttr.
     */
    @RequestMapping(value = "/productAttrs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductAttr> getProductAttr(@PathVariable Long id) {
        log.debug("REST request to get ProductAttr : {}", id);
        ProductAttr productAttr = productAttrRepository.findOne(id);
        return Optional.ofNullable(productAttr)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /productAttrs/:id -> delete the "id" productAttr.
     */
    @RequestMapping(value = "/productAttrs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductAttr(@PathVariable Long id) {
        log.debug("REST request to delete ProductAttr : {}", id);
        productAttrRepository.delete(id);
        productAttrSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productAttr", id.toString())).build();
    }

    /**
     * SEARCH  /_search/productAttrs/:query -> search for the productAttr corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/productAttrs/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductAttr> searchProductAttrs(@PathVariable String query) {
        log.debug("REST request to search ProductAttrs for query {}", query);
        return StreamSupport
            .stream(productAttrSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
