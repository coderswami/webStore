package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.Catalog;
import com.tl.webstore.repository.CatalogRepository;
import com.tl.webstore.repository.search.CatalogSearchRepository;
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
 * REST controller for managing Catalog.
 */
@RestController
@RequestMapping("/api")
public class CatalogResource {

    private final Logger log = LoggerFactory.getLogger(CatalogResource.class);
        
    @Inject
    private CatalogRepository catalogRepository;
    
    @Inject
    private CatalogSearchRepository catalogSearchRepository;
    
    /**
     * POST  /catalogs -> Create a new catalog.
     */
    @RequestMapping(value = "/catalogs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Catalog> createCatalog(@Valid @RequestBody Catalog catalog) throws URISyntaxException {
        log.debug("REST request to save Catalog : {}", catalog);
        if (catalog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("catalog", "idexists", "A new catalog cannot already have an ID")).body(null);
        }
        Catalog result = catalogRepository.save(catalog);
        catalogSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/catalogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("catalog", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /catalogs -> Updates an existing catalog.
     */
    @RequestMapping(value = "/catalogs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Catalog> updateCatalog(@Valid @RequestBody Catalog catalog) throws URISyntaxException {
        log.debug("REST request to update Catalog : {}", catalog);
        if (catalog.getId() == null) {
            return createCatalog(catalog);
        }
        Catalog result = catalogRepository.save(catalog);
        catalogSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("catalog", catalog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /catalogs -> get all the catalogs.
     */
    @RequestMapping(value = "/catalogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Catalog> getAllCatalogs() {
        log.debug("REST request to get all Catalogs");
        return catalogRepository.findAll();
            }

    /**
     * GET  /catalogs/:id -> get the "id" catalog.
     */
    @RequestMapping(value = "/catalogs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Catalog> getCatalog(@PathVariable Long id) {
        log.debug("REST request to get Catalog : {}", id);
        Catalog catalog = catalogRepository.findOne(id);
        return Optional.ofNullable(catalog)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /catalogs/:id -> delete the "id" catalog.
     */
    @RequestMapping(value = "/catalogs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long id) {
        log.debug("REST request to delete Catalog : {}", id);
        catalogRepository.delete(id);
        catalogSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("catalog", id.toString())).build();
    }

    /**
     * SEARCH  /_search/catalogs/:query -> search for the catalog corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/catalogs/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Catalog> searchCatalogs(@PathVariable String query) {
        log.debug("REST request to search Catalogs for query {}", query);
        return StreamSupport
            .stream(catalogSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
