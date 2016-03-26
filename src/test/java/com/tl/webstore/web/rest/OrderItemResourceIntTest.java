package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.OrderItem;
import com.tl.webstore.repository.OrderItemRepository;
import com.tl.webstore.repository.search.OrderItemSearchRepository;

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
 * Test class for the OrderItemResource REST controller.
 *
 * @see OrderItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrderItemResourceIntTest {


    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    
    private static final Status DEFAULT_STATUS = Status.AVAILABLE;
    private static final Status UPDATED_STATUS = Status.IN_STOCK;

    @Inject
    private OrderItemRepository orderItemRepository;

    @Inject
    private OrderItemSearchRepository orderItemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrderItemMockMvc;

    private OrderItem orderItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderItemResource orderItemResource = new OrderItemResource();
        ReflectionTestUtils.setField(orderItemResource, "orderItemSearchRepository", orderItemSearchRepository);
        ReflectionTestUtils.setField(orderItemResource, "orderItemRepository", orderItemRepository);
        this.restOrderItemMockMvc = MockMvcBuilders.standaloneSetup(orderItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        orderItem = new OrderItem();
        orderItem.setQuantity(DEFAULT_QUANTITY);
        orderItem.setPrice(DEFAULT_PRICE);
        orderItem.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createOrderItem() throws Exception {
        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();

        // Create the OrderItem

        restOrderItemMockMvc.perform(post("/api/orderItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItem)))
                .andExpect(status().isCreated());

        // Validate the OrderItem in the database
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeCreate + 1);
        OrderItem testOrderItem = orderItems.get(orderItems.size() - 1);
        assertThat(testOrderItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrderItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrderItem.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setQuantity(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc.perform(post("/api/orderItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItem)))
                .andExpect(status().isBadRequest());

        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setPrice(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc.perform(post("/api/orderItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItem)))
                .andExpect(status().isBadRequest());

        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setStatus(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc.perform(post("/api/orderItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItem)))
                .andExpect(status().isBadRequest());

        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItems
        restOrderItemMockMvc.perform(get("/api/orderItems?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get the orderItem
        restOrderItemMockMvc.perform(get("/api/orderItems/{id}", orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(orderItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get("/api/orderItems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

		int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem
        orderItem.setQuantity(UPDATED_QUANTITY);
        orderItem.setPrice(UPDATED_PRICE);
        orderItem.setStatus(UPDATED_STATUS);

        restOrderItemMockMvc.perform(put("/api/orderItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItem)))
                .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItems.get(orderItems.size() - 1);
        assertThat(testOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

		int databaseSizeBeforeDelete = orderItemRepository.findAll().size();

        // Get the orderItem
        restOrderItemMockMvc.perform(delete("/api/orderItems/{id}", orderItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
