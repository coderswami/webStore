package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.UserProfile;
import com.tl.webstore.repository.UserProfileRepository;
import com.tl.webstore.repository.search.UserProfileSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tl.webstore.domain.enumeration.Gender;

/**
 * Test class for the UserProfileResource REST controller.
 *
 * @see UserProfileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserProfileResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_FNAME = "AAAAA";
    private static final String UPDATED_FNAME = "BBBBB";
    private static final String DEFAULT_LNAME = "AAAAA";
    private static final String UPDATED_LNAME = "BBBBB";
    private static final String DEFAULT_IMAGE_URL = "AAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBB";
    
    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private UserProfileRepository userProfileRepository;

    @Inject
    private UserProfileSearchRepository userProfileSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserProfileResource userProfileResource = new UserProfileResource();
        ReflectionTestUtils.setField(userProfileResource, "userProfileSearchRepository", userProfileSearchRepository);
        ReflectionTestUtils.setField(userProfileResource, "userProfileRepository", userProfileRepository);
        this.restUserProfileMockMvc = MockMvcBuilders.standaloneSetup(userProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userProfile = new UserProfile();
        userProfile.setEmail(DEFAULT_EMAIL);
        userProfile.setFname(DEFAULT_FNAME);
        userProfile.setLname(DEFAULT_LNAME);
        userProfile.setImageUrl(DEFAULT_IMAGE_URL);
        userProfile.setGender(DEFAULT_GENDER);
        userProfile.setDob(DEFAULT_DOB);
    }

    @Test
    @Transactional
    public void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile

        restUserProfileMockMvc.perform(post("/api/userProfiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userProfile)))
                .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertThat(userProfiles).hasSize(databaseSizeBeforeCreate + 1);
        UserProfile testUserProfile = userProfiles.get(userProfiles.size() - 1);
        assertThat(testUserProfile.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserProfile.getFname()).isEqualTo(DEFAULT_FNAME);
        assertThat(testUserProfile.getLname()).isEqualTo(DEFAULT_LNAME);
        assertThat(testUserProfile.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testUserProfile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testUserProfile.getDob()).isEqualTo(DEFAULT_DOB);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setEmail(null);

        // Create the UserProfile, which fails.

        restUserProfileMockMvc.perform(post("/api/userProfiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userProfile)))
                .andExpect(status().isBadRequest());

        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertThat(userProfiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserProfiles() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfiles
        restUserProfileMockMvc.perform(get("/api/userProfiles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].fname").value(hasItem(DEFAULT_FNAME.toString())))
                .andExpect(jsonPath("$.[*].lname").value(hasItem(DEFAULT_LNAME.toString())))
                .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
                .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())));
    }

    @Test
    @Transactional
    public void getUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/userProfiles/{id}", userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.fname").value(DEFAULT_FNAME.toString()))
            .andExpect(jsonPath("$.lname").value(DEFAULT_LNAME.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/userProfiles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

		int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile
        userProfile.setEmail(UPDATED_EMAIL);
        userProfile.setFname(UPDATED_FNAME);
        userProfile.setLname(UPDATED_LNAME);
        userProfile.setImageUrl(UPDATED_IMAGE_URL);
        userProfile.setGender(UPDATED_GENDER);
        userProfile.setDob(UPDATED_DOB);

        restUserProfileMockMvc.perform(put("/api/userProfiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userProfile)))
                .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertThat(userProfiles).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfiles.get(userProfiles.size() - 1);
        assertThat(testUserProfile.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserProfile.getFname()).isEqualTo(UPDATED_FNAME);
        assertThat(testUserProfile.getLname()).isEqualTo(UPDATED_LNAME);
        assertThat(testUserProfile.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testUserProfile.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUserProfile.getDob()).isEqualTo(UPDATED_DOB);
    }

    @Test
    @Transactional
    public void deleteUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

		int databaseSizeBeforeDelete = userProfileRepository.findAll().size();

        // Get the userProfile
        restUserProfileMockMvc.perform(delete("/api/userProfiles/{id}", userProfile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertThat(userProfiles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
