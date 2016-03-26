package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.Currency;
import com.tl.webstore.repository.CurrencyRepository;
import com.tl.webstore.repository.search.CurrencySearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CurrencyResource REST controller.
 *
 * @see CurrencyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CurrencyResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private CurrencyRepository currencyRepository;

    @Inject
    private CurrencySearchRepository currencySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCurrencyMockMvc;

    private Currency currency;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CurrencyResource currencyResource = new CurrencyResource();
        ReflectionTestUtils.setField(currencyResource, "currencySearchRepository", currencySearchRepository);
        ReflectionTestUtils.setField(currencyResource, "currencyRepository", currencyRepository);
        this.restCurrencyMockMvc = MockMvcBuilders.standaloneSetup(currencyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        currency = new Currency();
        currency.setCode(DEFAULT_CODE);
        currency.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCurrency() throws Exception {
        int databaseSizeBeforeCreate = currencyRepository.findAll().size();

        // Create the Currency

        restCurrencyMockMvc.perform(post("/api/currencys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(currency)))
                .andExpect(status().isCreated());

        // Validate the Currency in the database
        List<Currency> currencys = currencyRepository.findAll();
        assertThat(currencys).hasSize(databaseSizeBeforeCreate + 1);
        Currency testCurrency = currencys.get(currencys.size() - 1);
        assertThat(testCurrency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCurrency.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setCode(null);

        // Create the Currency, which fails.

        restCurrencyMockMvc.perform(post("/api/currencys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(currency)))
                .andExpect(status().isBadRequest());

        List<Currency> currencys = currencyRepository.findAll();
        assertThat(currencys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCurrencys() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencys
        restCurrencyMockMvc.perform(get("/api/currencys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get the currency
        restCurrencyMockMvc.perform(get("/api/currencys/{id}", currency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(currency.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCurrency() throws Exception {
        // Get the currency
        restCurrencyMockMvc.perform(get("/api/currencys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

		int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency
        currency.setCode(UPDATED_CODE);
        currency.setDescription(UPDATED_DESCRIPTION);

        restCurrencyMockMvc.perform(put("/api/currencys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(currency)))
                .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencys = currencyRepository.findAll();
        assertThat(currencys).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencys.get(currencys.size() - 1);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCurrency.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

		int databaseSizeBeforeDelete = currencyRepository.findAll().size();

        // Get the currency
        restCurrencyMockMvc.perform(delete("/api/currencys/{id}", currency.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Currency> currencys = currencyRepository.findAll();
        assertThat(currencys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
