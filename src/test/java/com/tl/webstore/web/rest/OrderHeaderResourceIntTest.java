package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.OrderHeader;
import com.tl.webstore.repository.OrderHeaderRepository;
import com.tl.webstore.repository.search.OrderHeaderSearchRepository;

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

import com.tl.webstore.domain.enumeration.OrderType;
import com.tl.webstore.domain.enumeration.Status;

/**
 * Test class for the OrderHeaderResource REST controller.
 *
 * @see OrderHeaderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrderHeaderResourceIntTest {

    
    private static final OrderType DEFAULT_TYPE = OrderType.WISHLIST;
    private static final OrderType UPDATED_TYPE = OrderType.CART;
    
    private static final Status DEFAULT_STATUS = Status.AVAILABLE;
    private static final Status UPDATED_STATUS = Status.IN_STOCK;

    private static final Double DEFAULT_ORDER_TOTAL = 1D;
    private static final Double UPDATED_ORDER_TOTAL = 2D;

    @Inject
    private OrderHeaderRepository orderHeaderRepository;

    @Inject
    private OrderHeaderSearchRepository orderHeaderSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrderHeaderMockMvc;

    private OrderHeader orderHeader;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderHeaderResource orderHeaderResource = new OrderHeaderResource();
        ReflectionTestUtils.setField(orderHeaderResource, "orderHeaderSearchRepository", orderHeaderSearchRepository);
        ReflectionTestUtils.setField(orderHeaderResource, "orderHeaderRepository", orderHeaderRepository);
        this.restOrderHeaderMockMvc = MockMvcBuilders.standaloneSetup(orderHeaderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        orderHeader = new OrderHeader();
        orderHeader.setType(DEFAULT_TYPE);
        orderHeader.setStatus(DEFAULT_STATUS);
        orderHeader.setOrderTotal(DEFAULT_ORDER_TOTAL);
    }

    @Test
    @Transactional
    public void createOrderHeader() throws Exception {
        int databaseSizeBeforeCreate = orderHeaderRepository.findAll().size();

        // Create the OrderHeader

        restOrderHeaderMockMvc.perform(post("/api/orderHeaders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderHeader)))
                .andExpect(status().isCreated());

        // Validate the OrderHeader in the database
        List<OrderHeader> orderHeaders = orderHeaderRepository.findAll();
        assertThat(orderHeaders).hasSize(databaseSizeBeforeCreate + 1);
        OrderHeader testOrderHeader = orderHeaders.get(orderHeaders.size() - 1);
        assertThat(testOrderHeader.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOrderHeader.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrderHeader.getOrderTotal()).isEqualTo(DEFAULT_ORDER_TOTAL);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderHeaderRepository.findAll().size();
        // set the field null
        orderHeader.setType(null);

        // Create the OrderHeader, which fails.

        restOrderHeaderMockMvc.perform(post("/api/orderHeaders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderHeader)))
                .andExpect(status().isBadRequest());

        List<OrderHeader> orderHeaders = orderHeaderRepository.findAll();
        assertThat(orderHeaders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderHeaderRepository.findAll().size();
        // set the field null
        orderHeader.setStatus(null);

        // Create the OrderHeader, which fails.

        restOrderHeaderMockMvc.perform(post("/api/orderHeaders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderHeader)))
                .andExpect(status().isBadRequest());

        List<OrderHeader> orderHeaders = orderHeaderRepository.findAll();
        assertThat(orderHeaders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderHeaderRepository.findAll().size();
        // set the field null
        orderHeader.setOrderTotal(null);

        // Create the OrderHeader, which fails.

        restOrderHeaderMockMvc.perform(post("/api/orderHeaders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderHeader)))
                .andExpect(status().isBadRequest());

        List<OrderHeader> orderHeaders = orderHeaderRepository.findAll();
        assertThat(orderHeaders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderHeaders() throws Exception {
        // Initialize the database
        orderHeaderRepository.saveAndFlush(orderHeader);

        // Get all the orderHeaders
        restOrderHeaderMockMvc.perform(get("/api/orderHeaders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderHeader.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].orderTotal").value(hasItem(DEFAULT_ORDER_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    public void getOrderHeader() throws Exception {
        // Initialize the database
        orderHeaderRepository.saveAndFlush(orderHeader);

        // Get the orderHeader
        restOrderHeaderMockMvc.perform(get("/api/orderHeaders/{id}", orderHeader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(orderHeader.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.orderTotal").value(DEFAULT_ORDER_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderHeader() throws Exception {
        // Get the orderHeader
        restOrderHeaderMockMvc.perform(get("/api/orderHeaders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderHeader() throws Exception {
        // Initialize the database
        orderHeaderRepository.saveAndFlush(orderHeader);

		int databaseSizeBeforeUpdate = orderHeaderRepository.findAll().size();

        // Update the orderHeader
        orderHeader.setType(UPDATED_TYPE);
        orderHeader.setStatus(UPDATED_STATUS);
        orderHeader.setOrderTotal(UPDATED_ORDER_TOTAL);

        restOrderHeaderMockMvc.perform(put("/api/orderHeaders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderHeader)))
                .andExpect(status().isOk());

        // Validate the OrderHeader in the database
        List<OrderHeader> orderHeaders = orderHeaderRepository.findAll();
        assertThat(orderHeaders).hasSize(databaseSizeBeforeUpdate);
        OrderHeader testOrderHeader = orderHeaders.get(orderHeaders.size() - 1);
        assertThat(testOrderHeader.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrderHeader.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderHeader.getOrderTotal()).isEqualTo(UPDATED_ORDER_TOTAL);
    }

    @Test
    @Transactional
    public void deleteOrderHeader() throws Exception {
        // Initialize the database
        orderHeaderRepository.saveAndFlush(orderHeader);

		int databaseSizeBeforeDelete = orderHeaderRepository.findAll().size();

        // Get the orderHeader
        restOrderHeaderMockMvc.perform(delete("/api/orderHeaders/{id}", orderHeader.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderHeader> orderHeaders = orderHeaderRepository.findAll();
        assertThat(orderHeaders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
