package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.Product;
import com.tl.webstore.domain.ProductPrice;
import com.tl.webstore.repository.ProductPriceRepository;
import com.tl.webstore.repository.ProductRepository;
import com.tl.webstore.repository.search.ProductPriceSearchRepository;
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
 * REST controller for managing ProductPrice.
 */
@RestController
@RequestMapping("/api")
public class ProductPriceResource {

    private final Logger log = LoggerFactory.getLogger(ProductPriceResource.class);

    @Inject
    private ProductPriceRepository productPriceRepository;

    @Inject
    private ProductPriceSearchRepository productPriceSearchRepository;
    @Inject
    private ProductRepository productRepository;


    /**
     * POST  /productPrices -> Create a new productPrice.
     */
    @RequestMapping(value = "/productPrices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductPrice> createProductPrice(@Valid @RequestBody ProductPrice productPrice) throws URISyntaxException {
        log.debug("REST request to save ProductPrice : {}", productPrice);
        if (productPrice.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productPrice", "idexists", "A new productPrice cannot already have an ID")).body(null);
        }
        List<ProductPrice> getAllProductPrice = productPriceRepository.findAll();
        for(ProductPrice ListProductPrice : getAllProductPrice) {
        	if(ListProductPrice.getProduct().getId() == productPrice.getProduct().getId() && ListProductPrice.getActive()== true) {
        		productPrice.setActive(false);
        	}
        }
        ProductPrice result = productPriceRepository.save(productPrice);
        productPriceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/productPrices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productPrice", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /productPrices -> Updates an existing productPrice.
     */
    @RequestMapping(value = "/productPrices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductPrice> updateProductPrice(@Valid @RequestBody ProductPrice productPrice) throws URISyntaxException {
        log.debug("REST request to update ProductPrice : {}", productPrice);
        if (productPrice.getId() == null) {
            return createProductPrice(productPrice);
        }
        ProductPrice result = productPriceRepository.save(productPrice);
        productPriceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productPrice", productPrice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /productPrices -> get all the productPrices.
     */
    @RequestMapping(value = "/productPrices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductPrice> getAllProductPrices() {
        log.debug("REST request to get all ProductPrices");
        return productPriceRepository.findAll();
            }

    /**
     * GET  /productPrices/:id -> get the "id" productPrice.
     */
    @RequestMapping(value = "/productPrices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductPrice> getProductPrice(@PathVariable Long id) {
        log.debug("REST request to get ProductPrice : {}", id);
        ProductPrice productPrice = productPriceRepository.findOne(id);
        return Optional.ofNullable(productPrice)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /productPrices/product/:productId -> get the "productId" productPrice.
     */
    @RequestMapping(value = "/productPrices/product/{productId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductPrice> getProductActivePriceByProductId(@PathVariable Long productId) {
        log.debug("REST request to get ProductActivePrice : {}", productId);
        Product product = productRepository.findOne(productId);
        System.out.println("product is:"+ ""+ product);
        ProductPrice ListProductPrice = productPriceRepository.findByActiveAndProduct(true, product);
        System.out.println("List of product price is::"+ ""+ ListProductPrice);
        return Optional.ofNullable(ListProductPrice)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /productPrices/:id -> delete the "id" productPrice.
     */
    @RequestMapping(value = "/productPrices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductPrice(@PathVariable Long id) {
        log.debug("REST request to delete ProductPrice : {}", id);
        productPriceRepository.delete(id);
        productPriceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productPrice", id.toString())).build();
    }

    /**
     * SEARCH  /_search/productPrices/:query -> search for the productPrice corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/productPrices/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductPrice> searchProductPrices(@PathVariable String query) {
        log.debug("REST request to search ProductPrices for query {}", query);
        return StreamSupport
            .stream(productPriceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
