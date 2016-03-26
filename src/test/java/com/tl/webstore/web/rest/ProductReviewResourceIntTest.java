package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.ProductReview;
import com.tl.webstore.repository.ProductReviewRepository;
import com.tl.webstore.repository.search.ProductReviewSearchRepository;

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
 * Test class for the ProductReviewResource REST controller.
 *
 * @see ProductReviewResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProductReviewResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;

    @Inject
    private ProductReviewRepository productReviewRepository;

    @Inject
    private ProductReviewSearchRepository productReviewSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductReviewMockMvc;

    private ProductReview productReview;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductReviewResource productReviewResource = new ProductReviewResource();
        ReflectionTestUtils.setField(productReviewResource, "productReviewSearchRepository", productReviewSearchRepository);
        ReflectionTestUtils.setField(productReviewResource, "productReviewRepository", productReviewRepository);
        this.restProductReviewMockMvc = MockMvcBuilders.standaloneSetup(productReviewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        productReview = new ProductReview();
        productReview.setTitle(DEFAULT_TITLE);
        productReview.setDescription(DEFAULT_DESCRIPTION);
        productReview.setRating(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void createProductReview() throws Exception {
        int databaseSizeBeforeCreate = productReviewRepository.findAll().size();

        // Create the ProductReview

        restProductReviewMockMvc.perform(post("/api/productReviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productReview)))
                .andExpect(status().isCreated());

        // Validate the ProductReview in the database
        List<ProductReview> productReviews = productReviewRepository.findAll();
        assertThat(productReviews).hasSize(databaseSizeBeforeCreate + 1);
        ProductReview testProductReview = productReviews.get(productReviews.size() - 1);
        assertThat(testProductReview.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProductReview.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductReview.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = productReviewRepository.findAll().size();
        // set the field null
        productReview.setTitle(null);

        // Create the ProductReview, which fails.

        restProductReviewMockMvc.perform(post("/api/productReviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productReview)))
                .andExpect(status().isBadRequest());

        List<ProductReview> productReviews = productReviewRepository.findAll();
        assertThat(productReviews).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productReviewRepository.findAll().size();
        // set the field null
        productReview.setDescription(null);

        // Create the ProductReview, which fails.

        restProductReviewMockMvc.perform(post("/api/productReviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productReview)))
                .andExpect(status().isBadRequest());

        List<ProductReview> productReviews = productReviewRepository.findAll();
        assertThat(productReviews).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = productReviewRepository.findAll().size();
        // set the field null
        productReview.setRating(null);

        // Create the ProductReview, which fails.

        restProductReviewMockMvc.perform(post("/api/productReviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productReview)))
                .andExpect(status().isBadRequest());

        List<ProductReview> productReviews = productReviewRepository.findAll();
        assertThat(productReviews).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductReviews() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviews
        restProductReviewMockMvc.perform(get("/api/productReviews?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productReview.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())));
    }

    @Test
    @Transactional
    public void getProductReview() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        // Get the productReview
        restProductReviewMockMvc.perform(get("/api/productReviews/{id}", productReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(productReview.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProductReview() throws Exception {
        // Get the productReview
        restProductReviewMockMvc.perform(get("/api/productReviews/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductReview() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

		int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();

        // Update the productReview
        productReview.setTitle(UPDATED_TITLE);
        productReview.setDescription(UPDATED_DESCRIPTION);
        productReview.setRating(UPDATED_RATING);

        restProductReviewMockMvc.perform(put("/api/productReviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productReview)))
                .andExpect(status().isOk());

        // Validate the ProductReview in the database
        List<ProductReview> productReviews = productReviewRepository.findAll();
        assertThat(productReviews).hasSize(databaseSizeBeforeUpdate);
        ProductReview testProductReview = productReviews.get(productReviews.size() - 1);
        assertThat(testProductReview.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProductReview.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductReview.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    public void deleteProductReview() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

		int databaseSizeBeforeDelete = productReviewRepository.findAll().size();

        // Get the productReview
        restProductReviewMockMvc.perform(delete("/api/productReviews/{id}", productReview.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductReview> productReviews = productReviewRepository.findAll();
        assertThat(productReviews).hasSize(databaseSizeBeforeDelete - 1);
    }
}
