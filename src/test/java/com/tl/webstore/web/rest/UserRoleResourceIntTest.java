package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.UserRole;
import com.tl.webstore.repository.UserRoleRepository;
import com.tl.webstore.repository.search.UserRoleSearchRepository;

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
 * Test class for the UserRoleResource REST controller.
 *
 * @see UserRoleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserRoleResourceIntTest {

    private static final String DEFAULT_ROLE = "AAAAA";
    private static final String UPDATED_ROLE = "BBBBB";

    @Inject
    private UserRoleRepository userRoleRepository;

    @Inject
    private UserRoleSearchRepository userRoleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserRoleMockMvc;

    private UserRole userRole;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserRoleResource userRoleResource = new UserRoleResource();
        ReflectionTestUtils.setField(userRoleResource, "userRoleSearchRepository", userRoleSearchRepository);
        ReflectionTestUtils.setField(userRoleResource, "userRoleRepository", userRoleRepository);
        this.restUserRoleMockMvc = MockMvcBuilders.standaloneSetup(userRoleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userRole = new UserRole();
        userRole.setRole(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createUserRole() throws Exception {
        int databaseSizeBeforeCreate = userRoleRepository.findAll().size();

        // Create the UserRole

        restUserRoleMockMvc.perform(post("/api/userRoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userRole)))
                .andExpect(status().isCreated());

        // Validate the UserRole in the database
        List<UserRole> userRoles = userRoleRepository.findAll();
        assertThat(userRoles).hasSize(databaseSizeBeforeCreate + 1);
        UserRole testUserRole = userRoles.get(userRoles.size() - 1);
        assertThat(testUserRole.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRoleRepository.findAll().size();
        // set the field null
        userRole.setRole(null);

        // Create the UserRole, which fails.

        restUserRoleMockMvc.perform(post("/api/userRoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userRole)))
                .andExpect(status().isBadRequest());

        List<UserRole> userRoles = userRoleRepository.findAll();
        assertThat(userRoles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserRoles() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoles
        restUserRoleMockMvc.perform(get("/api/userRoles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userRole.getId().intValue())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @Test
    @Transactional
    public void getUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        // Get the userRole
        restUserRoleMockMvc.perform(get("/api/userRoles/{id}", userRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userRole.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserRole() throws Exception {
        // Get the userRole
        restUserRoleMockMvc.perform(get("/api/userRoles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

		int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();

        // Update the userRole
        userRole.setRole(UPDATED_ROLE);

        restUserRoleMockMvc.perform(put("/api/userRoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userRole)))
                .andExpect(status().isOk());

        // Validate the UserRole in the database
        List<UserRole> userRoles = userRoleRepository.findAll();
        assertThat(userRoles).hasSize(databaseSizeBeforeUpdate);
        UserRole testUserRole = userRoles.get(userRoles.size() - 1);
        assertThat(testUserRole.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void deleteUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

		int databaseSizeBeforeDelete = userRoleRepository.findAll().size();

        // Get the userRole
        restUserRoleMockMvc.perform(delete("/api/userRoles/{id}", userRole.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserRole> userRoles = userRoleRepository.findAll();
        assertThat(userRoles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
