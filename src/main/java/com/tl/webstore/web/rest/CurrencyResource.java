package com.tl.webstore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tl.webstore.domain.Currency;
import com.tl.webstore.repository.CurrencyRepository;
import com.tl.webstore.repository.search.CurrencySearchRepository;
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
 * REST controller for managing Currency.
 */
@RestController
@RequestMapping("/api")
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);
        
    @Inject
    private CurrencyRepository currencyRepository;
    
    @Inject
    private CurrencySearchRepository currencySearchRepository;
    
    /**
     * POST  /currencys -> Create a new currency.
     */
    @RequestMapping(value = "/currencys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Currency> createCurrency(@Valid @RequestBody Currency currency) throws URISyntaxException {
        log.debug("REST request to save Currency : {}", currency);
        if (currency.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("currency", "idexists", "A new currency cannot already have an ID")).body(null);
        }
        Currency result = currencyRepository.save(currency);
        currencySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/currencys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("currency", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /currencys -> Updates an existing currency.
     */
    @RequestMapping(value = "/currencys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Currency> updateCurrency(@Valid @RequestBody Currency currency) throws URISyntaxException {
        log.debug("REST request to update Currency : {}", currency);
        if (currency.getId() == null) {
            return createCurrency(currency);
        }
        Currency result = currencyRepository.save(currency);
        currencySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("currency", currency.getId().toString()))
            .body(result);
    }

    /**
     * GET  /currencys -> get all the currencys.
     */
    @RequestMapping(value = "/currencys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Currency> getAllCurrencys() {
        log.debug("REST request to get all Currencys");
        return currencyRepository.findAll();
            }

    /**
     * GET  /currencys/:id -> get the "id" currency.
     */
    @RequestMapping(value = "/currencys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Currency> getCurrency(@PathVariable Long id) {
        log.debug("REST request to get Currency : {}", id);
        Currency currency = currencyRepository.findOne(id);
        return Optional.ofNullable(currency)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /currencys/:id -> delete the "id" currency.
     */
    @RequestMapping(value = "/currencys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        log.debug("REST request to delete Currency : {}", id);
        currencyRepository.delete(id);
        currencySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("currency", id.toString())).build();
    }

    /**
     * SEARCH  /_search/currencys/:query -> search for the currency corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/currencys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Currency> searchCurrencys(@PathVariable String query) {
        log.debug("REST request to search Currencys for query {}", query);
        return StreamSupport
            .stream(currencySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
