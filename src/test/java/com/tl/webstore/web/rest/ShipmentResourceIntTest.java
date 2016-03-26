package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.Shipment;
import com.tl.webstore.repository.ShipmentRepository;
import com.tl.webstore.repository.search.ShipmentSearchRepository;

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

import com.tl.webstore.domain.enumeration.ShipmentType;
import com.tl.webstore.domain.enumeration.Status;

/**
 * Test class for the ShipmentResource REST controller.
 *
 * @see ShipmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ShipmentResourceIntTest {

    
    private static final ShipmentType DEFAULT_TYPE = ShipmentType.EXPRESS;
    private static final ShipmentType UPDATED_TYPE = ShipmentType.NORMAL;
    
    private static final Status DEFAULT_STATUS = Status.AVAILABLE;
    private static final Status UPDATED_STATUS = Status.IN_STOCK;

    @Inject
    private ShipmentRepository shipmentRepository;

    @Inject
    private ShipmentSearchRepository shipmentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restShipmentMockMvc;

    private Shipment shipment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShipmentResource shipmentResource = new ShipmentResource();
        ReflectionTestUtils.setField(shipmentResource, "shipmentSearchRepository", shipmentSearchRepository);
        ReflectionTestUtils.setField(shipmentResource, "shipmentRepository", shipmentRepository);
        this.restShipmentMockMvc = MockMvcBuilders.standaloneSetup(shipmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        shipment = new Shipment();
        shipment.setType(DEFAULT_TYPE);
        shipment.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createShipment() throws Exception {
        int databaseSizeBeforeCreate = shipmentRepository.findAll().size();

        // Create the Shipment

        restShipmentMockMvc.perform(post("/api/shipments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shipment)))
                .andExpect(status().isCreated());

        // Validate the Shipment in the database
        List<Shipment> shipments = shipmentRepository.findAll();
        assertThat(shipments).hasSize(databaseSizeBeforeCreate + 1);
        Shipment testShipment = shipments.get(shipments.size() - 1);
        assertThat(testShipment.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testShipment.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipmentRepository.findAll().size();
        // set the field null
        shipment.setType(null);

        // Create the Shipment, which fails.

        restShipmentMockMvc.perform(post("/api/shipments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shipment)))
                .andExpect(status().isBadRequest());

        List<Shipment> shipments = shipmentRepository.findAll();
        assertThat(shipments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipmentRepository.findAll().size();
        // set the field null
        shipment.setStatus(null);

        // Create the Shipment, which fails.

        restShipmentMockMvc.perform(post("/api/shipments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shipment)))
                .andExpect(status().isBadRequest());

        List<Shipment> shipments = shipmentRepository.findAll();
        assertThat(shipments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShipments() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get all the shipments
        restShipmentMockMvc.perform(get("/api/shipments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shipment.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getShipment() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

        // Get the shipment
        restShipmentMockMvc.perform(get("/api/shipments/{id}", shipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(shipment.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShipment() throws Exception {
        // Get the shipment
        restShipmentMockMvc.perform(get("/api/shipments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShipment() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

		int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();

        // Update the shipment
        shipment.setType(UPDATED_TYPE);
        shipment.setStatus(UPDATED_STATUS);

        restShipmentMockMvc.perform(put("/api/shipments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shipment)))
                .andExpect(status().isOk());

        // Validate the Shipment in the database
        List<Shipment> shipments = shipmentRepository.findAll();
        assertThat(shipments).hasSize(databaseSizeBeforeUpdate);
        Shipment testShipment = shipments.get(shipments.size() - 1);
        assertThat(testShipment.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testShipment.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteShipment() throws Exception {
        // Initialize the database
        shipmentRepository.saveAndFlush(shipment);

		int databaseSizeBeforeDelete = shipmentRepository.findAll().size();

        // Get the shipment
        restShipmentMockMvc.perform(delete("/api/shipments/{id}", shipment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Shipment> shipments = shipmentRepository.findAll();
        assertThat(shipments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
