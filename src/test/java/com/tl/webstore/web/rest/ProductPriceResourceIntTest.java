package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.ProductPrice;
import com.tl.webstore.repository.ProductPriceRepository;
import com.tl.webstore.repository.search.ProductPriceSearchRepository;

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
 * Test class for the ProductPriceResource REST controller.
 *
 * @see ProductPriceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProductPriceResourceIntTest {


    private static final Double DEFAULT_LIST_PRICE = 1D;
    private static final Double UPDATED_LIST_PRICE = 2D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Double DEFAULT_SALES_PRICE = 1D;
    private static final Double UPDATED_SALES_PRICE = 2D;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private ProductPriceRepository productPriceRepository;

    @Inject
    private ProductPriceSearchRepository productPriceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductPriceMockMvc;

    private ProductPrice productPrice;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductPriceResource productPriceResource = new ProductPriceResource();
        ReflectionTestUtils.setField(productPriceResource, "productPriceSearchRepository", productPriceSearchRepository);
        ReflectionTestUtils.setField(productPriceResource, "productPriceRepository", productPriceRepository);
        this.restProductPriceMockMvc = MockMvcBuilders.standaloneSetup(productPriceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        productPrice = new ProductPrice();
        productPrice.setListPrice(DEFAULT_LIST_PRICE);
        productPrice.setDiscount(DEFAULT_DISCOUNT);
        productPrice.setSalesPrice(DEFAULT_SALES_PRICE);
        productPrice.setActive(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createProductPrice() throws Exception {
        int databaseSizeBeforeCreate = productPriceRepository.findAll().size();

        // Create the ProductPrice

        restProductPriceMockMvc.perform(post("/api/productPrices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productPrice)))
                .andExpect(status().isCreated());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPrices = productPriceRepository.findAll();
        assertThat(productPrices).hasSize(databaseSizeBeforeCreate + 1);
        ProductPrice testProductPrice = productPrices.get(productPrices.size() - 1);
        assertThat(testProductPrice.getListPrice()).isEqualTo(DEFAULT_LIST_PRICE);
        assertThat(testProductPrice.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testProductPrice.getSalesPrice()).isEqualTo(DEFAULT_SALES_PRICE);
        assertThat(testProductPrice.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void checkListPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productPriceRepository.findAll().size();
        // set the field null
        productPrice.setListPrice(null);

        // Create the ProductPrice, which fails.

        restProductPriceMockMvc.perform(post("/api/productPrices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productPrice)))
                .andExpect(status().isBadRequest());

        List<ProductPrice> productPrices = productPriceRepository.findAll();
        assertThat(productPrices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = productPriceRepository.findAll().size();
        // set the field null
        productPrice.setActive(null);

        // Create the ProductPrice, which fails.

        restProductPriceMockMvc.perform(post("/api/productPrices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productPrice)))
                .andExpect(status().isBadRequest());

        List<ProductPrice> productPrices = productPriceRepository.findAll();
        assertThat(productPrices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductPrices() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPrices
        restProductPriceMockMvc.perform(get("/api/productPrices?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productPrice.getId().intValue())))
                .andExpect(jsonPath("$.[*].listPrice").value(hasItem(DEFAULT_LIST_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].salesPrice").value(hasItem(DEFAULT_SALES_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getProductPrice() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        // Get the productPrice
        restProductPriceMockMvc.perform(get("/api/productPrices/{id}", productPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(productPrice.getId().intValue()))
            .andExpect(jsonPath("$.listPrice").value(DEFAULT_LIST_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.salesPrice").value(DEFAULT_SALES_PRICE.doubleValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProductPrice() throws Exception {
        // Get the productPrice
        restProductPriceMockMvc.perform(get("/api/productPrices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductPrice() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

		int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();

        // Update the productPrice
        productPrice.setListPrice(UPDATED_LIST_PRICE);
        productPrice.setDiscount(UPDATED_DISCOUNT);
        productPrice.setSalesPrice(UPDATED_SALES_PRICE);
        productPrice.setActive(UPDATED_ACTIVE);

        restProductPriceMockMvc.perform(put("/api/productPrices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productPrice)))
                .andExpect(status().isOk());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPrices = productPriceRepository.findAll();
        assertThat(productPrices).hasSize(databaseSizeBeforeUpdate);
        ProductPrice testProductPrice = productPrices.get(productPrices.size() - 1);
        assertThat(testProductPrice.getListPrice()).isEqualTo(UPDATED_LIST_PRICE);
        assertThat(testProductPrice.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testProductPrice.getSalesPrice()).isEqualTo(UPDATED_SALES_PRICE);
        assertThat(testProductPrice.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void deleteProductPrice() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

		int databaseSizeBeforeDelete = productPriceRepository.findAll().size();

        // Get the productPrice
        restProductPriceMockMvc.perform(delete("/api/productPrices/{id}", productPrice.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductPrice> productPrices = productPriceRepository.findAll();
        assertThat(productPrices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
