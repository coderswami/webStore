package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.UserLogin;
import com.tl.webstore.repository.UserLoginRepository;
import com.tl.webstore.repository.search.UserLoginSearchRepository;

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
 * Test class for the UserLoginResource REST controller.
 *
 * @see UserLoginResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserLoginResourceIntTest {

    private static final String DEFAULT_USERNAME = "AAAAA";
    private static final String UPDATED_USERNAME = "BBBBB";
    private static final String DEFAULT_PASSWORD = "AAAAA";
    private static final String UPDATED_PASSWORD = "BBBBB";

    @Inject
    private UserLoginRepository userLoginRepository;

    @Inject
    private UserLoginSearchRepository userLoginSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserLoginMockMvc;

    private UserLogin userLogin;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserLoginResource userLoginResource = new UserLoginResource();
        ReflectionTestUtils.setField(userLoginResource, "userLoginSearchRepository", userLoginSearchRepository);
        ReflectionTestUtils.setField(userLoginResource, "userLoginRepository", userLoginRepository);
        this.restUserLoginMockMvc = MockMvcBuilders.standaloneSetup(userLoginResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userLogin = new UserLogin();
        userLogin.setUsername(DEFAULT_USERNAME);
        userLogin.setPassword(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void createUserLogin() throws Exception {
        int databaseSizeBeforeCreate = userLoginRepository.findAll().size();

        // Create the UserLogin

        restUserLoginMockMvc.perform(post("/api/userLogins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userLogin)))
                .andExpect(status().isCreated());

        // Validate the UserLogin in the database
        List<UserLogin> userLogins = userLoginRepository.findAll();
        assertThat(userLogins).hasSize(databaseSizeBeforeCreate + 1);
        UserLogin testUserLogin = userLogins.get(userLogins.size() - 1);
        assertThat(testUserLogin.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUserLogin.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLoginRepository.findAll().size();
        // set the field null
        userLogin.setUsername(null);

        // Create the UserLogin, which fails.

        restUserLoginMockMvc.perform(post("/api/userLogins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userLogin)))
                .andExpect(status().isBadRequest());

        List<UserLogin> userLogins = userLoginRepository.findAll();
        assertThat(userLogins).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLoginRepository.findAll().size();
        // set the field null
        userLogin.setPassword(null);

        // Create the UserLogin, which fails.

        restUserLoginMockMvc.perform(post("/api/userLogins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userLogin)))
                .andExpect(status().isBadRequest());

        List<UserLogin> userLogins = userLoginRepository.findAll();
        assertThat(userLogins).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserLogins() throws Exception {
        // Initialize the database
        userLoginRepository.saveAndFlush(userLogin);

        // Get all the userLogins
        restUserLoginMockMvc.perform(get("/api/userLogins?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userLogin.getId().intValue())))
                .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void getUserLogin() throws Exception {
        // Initialize the database
        userLoginRepository.saveAndFlush(userLogin);

        // Get the userLogin
        restUserLoginMockMvc.perform(get("/api/userLogins/{id}", userLogin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userLogin.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserLogin() throws Exception {
        // Get the userLogin
        restUserLoginMockMvc.perform(get("/api/userLogins/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserLogin() throws Exception {
        // Initialize the database
        userLoginRepository.saveAndFlush(userLogin);

		int databaseSizeBeforeUpdate = userLoginRepository.findAll().size();

        // Update the userLogin
        userLogin.setUsername(UPDATED_USERNAME);
        userLogin.setPassword(UPDATED_PASSWORD);

        restUserLoginMockMvc.perform(put("/api/userLogins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userLogin)))
                .andExpect(status().isOk());

        // Validate the UserLogin in the database
        List<UserLogin> userLogins = userLoginRepository.findAll();
        assertThat(userLogins).hasSize(databaseSizeBeforeUpdate);
        UserLogin testUserLogin = userLogins.get(userLogins.size() - 1);
        assertThat(testUserLogin.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUserLogin.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void deleteUserLogin() throws Exception {
        // Initialize the database
        userLoginRepository.saveAndFlush(userLogin);

		int databaseSizeBeforeDelete = userLoginRepository.findAll().size();

        // Get the userLogin
        restUserLoginMockMvc.perform(delete("/api/userLogins/{id}", userLogin.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserLogin> userLogins = userLoginRepository.findAll();
        assertThat(userLogins).hasSize(databaseSizeBeforeDelete - 1);
    }
}
