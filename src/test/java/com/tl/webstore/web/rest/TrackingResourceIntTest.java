package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.Tracking;
import com.tl.webstore.repository.TrackingRepository;
import com.tl.webstore.repository.search.TrackingSearchRepository;

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

import com.tl.webstore.domain.enumeration.Status;

/**
 * Test class for the TrackingResource REST controller.
 *
 * @see TrackingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TrackingResourceIntTest {

    
    private static final Status DEFAULT_STATUS = Status.AVAILABLE;
    private static final Status UPDATED_STATUS = Status.IN_STOCK;
    private static final String DEFAULT_DETAILS = "AAAAA";
    private static final String UPDATED_DETAILS = "BBBBB";

    @Inject
    private TrackingRepository trackingRepository;

    @Inject
    private TrackingSearchRepository trackingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTrackingMockMvc;

    private Tracking tracking;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrackingResource trackingResource = new TrackingResource();
        ReflectionTestUtils.setField(trackingResource, "trackingSearchRepository", trackingSearchRepository);
        ReflectionTestUtils.setField(trackingResource, "trackingRepository", trackingRepository);
        this.restTrackingMockMvc = MockMvcBuilders.standaloneSetup(trackingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tracking = new Tracking();
        tracking.setStatus(DEFAULT_STATUS);
        tracking.setDetails(DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    public void createTracking() throws Exception {
        int databaseSizeBeforeCreate = trackingRepository.findAll().size();

        // Create the Tracking

        restTrackingMockMvc.perform(post("/api/trackings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tracking)))
                .andExpect(status().isCreated());

        // Validate the Tracking in the database
        List<Tracking> trackings = trackingRepository.findAll();
        assertThat(trackings).hasSize(databaseSizeBeforeCreate + 1);
        Tracking testTracking = trackings.get(trackings.size() - 1);
        assertThat(testTracking.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTracking.getDetails()).isEqualTo(DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingRepository.findAll().size();
        // set the field null
        tracking.setStatus(null);

        // Create the Tracking, which fails.

        restTrackingMockMvc.perform(post("/api/trackings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tracking)))
                .andExpect(status().isBadRequest());

        List<Tracking> trackings = trackingRepository.findAll();
        assertThat(trackings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrackings() throws Exception {
        // Initialize the database
        trackingRepository.saveAndFlush(tracking);

        // Get all the trackings
        restTrackingMockMvc.perform(get("/api/trackings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tracking.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())));
    }

    @Test
    @Transactional
    public void getTracking() throws Exception {
        // Initialize the database
        trackingRepository.saveAndFlush(tracking);

        // Get the tracking
        restTrackingMockMvc.perform(get("/api/trackings/{id}", tracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tracking.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTracking() throws Exception {
        // Get the tracking
        restTrackingMockMvc.perform(get("/api/trackings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTracking() throws Exception {
        // Initialize the database
        trackingRepository.saveAndFlush(tracking);

		int databaseSizeBeforeUpdate = trackingRepository.findAll().size();

        // Update the tracking
        tracking.setStatus(UPDATED_STATUS);
        tracking.setDetails(UPDATED_DETAILS);

        restTrackingMockMvc.perform(put("/api/trackings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tracking)))
                .andExpect(status().isOk());

        // Validate the Tracking in the database
        List<Tracking> trackings = trackingRepository.findAll();
        assertThat(trackings).hasSize(databaseSizeBeforeUpdate);
        Tracking testTracking = trackings.get(trackings.size() - 1);
        assertThat(testTracking.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTracking.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    public void deleteTracking() throws Exception {
        // Initialize the database
        trackingRepository.saveAndFlush(tracking);

		int databaseSizeBeforeDelete = trackingRepository.findAll().size();

        // Get the tracking
        restTrackingMockMvc.perform(delete("/api/trackings/{id}", tracking.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tracking> trackings = trackingRepository.findAll();
        assertThat(trackings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
