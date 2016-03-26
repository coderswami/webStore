package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.UserAddress;
import com.tl.webstore.repository.UserAddressRepository;
import com.tl.webstore.repository.search.UserAddressSearchRepository;

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
 * Test class for the UserAddressResource REST controller.
 *
 * @see UserAddressResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserAddressResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_STREET_ADDRESS = "AAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBB";
    private static final String DEFAULT_LANDMARK = "AAAAA";
    private static final String UPDATED_LANDMARK = "BBBBB";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_PIN = "AAAAA";
    private static final String UPDATED_PIN = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";

    @Inject
    private UserAddressRepository userAddressRepository;

    @Inject
    private UserAddressSearchRepository userAddressSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserAddressMockMvc;

    private UserAddress userAddress;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserAddressResource userAddressResource = new UserAddressResource();
        ReflectionTestUtils.setField(userAddressResource, "userAddressSearchRepository", userAddressSearchRepository);
        ReflectionTestUtils.setField(userAddressResource, "userAddressRepository", userAddressRepository);
        this.restUserAddressMockMvc = MockMvcBuilders.standaloneSetup(userAddressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userAddress = new UserAddress();
        userAddress.setName(DEFAULT_NAME);
        userAddress.setStreetAddress(DEFAULT_STREET_ADDRESS);
        userAddress.setLandmark(DEFAULT_LANDMARK);
        userAddress.setCity(DEFAULT_CITY);
        userAddress.setPin(DEFAULT_PIN);
        userAddress.setPhone(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createUserAddress() throws Exception {
        int databaseSizeBeforeCreate = userAddressRepository.findAll().size();

        // Create the UserAddress

        restUserAddressMockMvc.perform(post("/api/userAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userAddress)))
                .andExpect(status().isCreated());

        // Validate the UserAddress in the database
        List<UserAddress> userAddresss = userAddressRepository.findAll();
        assertThat(userAddresss).hasSize(databaseSizeBeforeCreate + 1);
        UserAddress testUserAddress = userAddresss.get(userAddresss.size() - 1);
        assertThat(testUserAddress.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserAddress.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testUserAddress.getLandmark()).isEqualTo(DEFAULT_LANDMARK);
        assertThat(testUserAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserAddress.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testUserAddress.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setName(null);

        // Create the UserAddress, which fails.

        restUserAddressMockMvc.perform(post("/api/userAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userAddress)))
                .andExpect(status().isBadRequest());

        List<UserAddress> userAddresss = userAddressRepository.findAll();
        assertThat(userAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setStreetAddress(null);

        // Create the UserAddress, which fails.

        restUserAddressMockMvc.perform(post("/api/userAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userAddress)))
                .andExpect(status().isBadRequest());

        List<UserAddress> userAddresss = userAddressRepository.findAll();
        assertThat(userAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setCity(null);

        // Create the UserAddress, which fails.

        restUserAddressMockMvc.perform(post("/api/userAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userAddress)))
                .andExpect(status().isBadRequest());

        List<UserAddress> userAddresss = userAddressRepository.findAll();
        assertThat(userAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPinIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setPin(null);

        // Create the UserAddress, which fails.

        restUserAddressMockMvc.perform(post("/api/userAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userAddress)))
                .andExpect(status().isBadRequest());

        List<UserAddress> userAddresss = userAddressRepository.findAll();
        assertThat(userAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setPhone(null);

        // Create the UserAddress, which fails.

        restUserAddressMockMvc.perform(post("/api/userAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userAddress)))
                .andExpect(status().isBadRequest());

        List<UserAddress> userAddresss = userAddressRepository.findAll();
        assertThat(userAddresss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserAddresss() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddresss
        restUserAddressMockMvc.perform(get("/api/userAddresss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userAddress.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }

    @Test
    @Transactional
    public void getUserAddress() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get the userAddress
        restUserAddressMockMvc.perform(get("/api/userAddresss/{id}", userAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userAddress.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.landmark").value(DEFAULT_LANDMARK.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserAddress() throws Exception {
        // Get the userAddress
        restUserAddressMockMvc.perform(get("/api/userAddresss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserAddress() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

		int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();

        // Update the userAddress
        userAddress.setName(UPDATED_NAME);
        userAddress.setStreetAddress(UPDATED_STREET_ADDRESS);
        userAddress.setLandmark(UPDATED_LANDMARK);
        userAddress.setCity(UPDATED_CITY);
        userAddress.setPin(UPDATED_PIN);
        userAddress.setPhone(UPDATED_PHONE);

        restUserAddressMockMvc.perform(put("/api/userAddresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userAddress)))
                .andExpect(status().isOk());

        // Validate the UserAddress in the database
        List<UserAddress> userAddresss = userAddressRepository.findAll();
        assertThat(userAddresss).hasSize(databaseSizeBeforeUpdate);
        UserAddress testUserAddress = userAddresss.get(userAddresss.size() - 1);
        assertThat(testUserAddress.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserAddress.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testUserAddress.getLandmark()).isEqualTo(UPDATED_LANDMARK);
        assertThat(testUserAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserAddress.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testUserAddress.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void deleteUserAddress() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

		int databaseSizeBeforeDelete = userAddressRepository.findAll().size();

        // Get the userAddress
        restUserAddressMockMvc.perform(delete("/api/userAddresss/{id}", userAddress.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserAddress> userAddresss = userAddressRepository.findAll();
        assertThat(userAddresss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
