package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.ProductAttr;
import com.tl.webstore.repository.ProductAttrRepository;
import com.tl.webstore.repository.search.ProductAttrSearchRepository;

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
 * Test class for the ProductAttrResource REST controller.
 *
 * @see ProductAttrResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProductAttrResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private ProductAttrRepository productAttrRepository;

    @Inject
    private ProductAttrSearchRepository productAttrSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductAttrMockMvc;

    private ProductAttr productAttr;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductAttrResource productAttrResource = new ProductAttrResource();
        ReflectionTestUtils.setField(productAttrResource, "productAttrSearchRepository", productAttrSearchRepository);
        ReflectionTestUtils.setField(productAttrResource, "productAttrRepository", productAttrRepository);
        this.restProductAttrMockMvc = MockMvcBuilders.standaloneSetup(productAttrResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        productAttr = new ProductAttr();
        productAttr.setName(DEFAULT_NAME);
        productAttr.setDescription(DEFAULT_DESCRIPTION);
        productAttr.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createProductAttr() throws Exception {
        int databaseSizeBeforeCreate = productAttrRepository.findAll().size();

        // Create the ProductAttr

        restProductAttrMockMvc.perform(post("/api/productAttrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productAttr)))
                .andExpect(status().isCreated());

        // Validate the ProductAttr in the database
        List<ProductAttr> productAttrs = productAttrRepository.findAll();
        assertThat(productAttrs).hasSize(databaseSizeBeforeCreate + 1);
        ProductAttr testProductAttr = productAttrs.get(productAttrs.size() - 1);
        assertThat(testProductAttr.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProductAttr.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductAttr.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productAttrRepository.findAll().size();
        // set the field null
        productAttr.setName(null);

        // Create the ProductAttr, which fails.

        restProductAttrMockMvc.perform(post("/api/productAttrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productAttr)))
                .andExpect(status().isBadRequest());

        List<ProductAttr> productAttrs = productAttrRepository.findAll();
        assertThat(productAttrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = productAttrRepository.findAll().size();
        // set the field null
        productAttr.setValue(null);

        // Create the ProductAttr, which fails.

        restProductAttrMockMvc.perform(post("/api/productAttrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productAttr)))
                .andExpect(status().isBadRequest());

        List<ProductAttr> productAttrs = productAttrRepository.findAll();
        assertThat(productAttrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductAttrs() throws Exception {
        // Initialize the database
        productAttrRepository.saveAndFlush(productAttr);

        // Get all the productAttrs
        restProductAttrMockMvc.perform(get("/api/productAttrs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productAttr.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getProductAttr() throws Exception {
        // Initialize the database
        productAttrRepository.saveAndFlush(productAttr);

        // Get the productAttr
        restProductAttrMockMvc.perform(get("/api/productAttrs/{id}", productAttr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(productAttr.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductAttr() throws Exception {
        // Get the productAttr
        restProductAttrMockMvc.perform(get("/api/productAttrs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductAttr() throws Exception {
        // Initialize the database
        productAttrRepository.saveAndFlush(productAttr);

		int databaseSizeBeforeUpdate = productAttrRepository.findAll().size();

        // Update the productAttr
        productAttr.setName(UPDATED_NAME);
        productAttr.setDescription(UPDATED_DESCRIPTION);
        productAttr.setValue(UPDATED_VALUE);

        restProductAttrMockMvc.perform(put("/api/productAttrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productAttr)))
                .andExpect(status().isOk());

        // Validate the ProductAttr in the database
        List<ProductAttr> productAttrs = productAttrRepository.findAll();
        assertThat(productAttrs).hasSize(databaseSizeBeforeUpdate);
        ProductAttr testProductAttr = productAttrs.get(productAttrs.size() - 1);
        assertThat(testProductAttr.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductAttr.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductAttr.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteProductAttr() throws Exception {
        // Initialize the database
        productAttrRepository.saveAndFlush(productAttr);

		int databaseSizeBeforeDelete = productAttrRepository.findAll().size();

        // Get the productAttr
        restProductAttrMockMvc.perform(delete("/api/productAttrs/{id}", productAttr.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductAttr> productAttrs = productAttrRepository.findAll();
        assertThat(productAttrs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
