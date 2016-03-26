package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.Category;
import com.tl.webstore.repository.CategoryRepository;
import com.tl.webstore.repository.search.CategorySearchRepository;

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
 * Test class for the CategoryResource REST controller.
 *
 * @see CategoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_IMAGE_URL = "AAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private CategorySearchRepository categorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCategoryMockMvc;

    private Category category;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CategoryResource categoryResource = new CategoryResource();
        ReflectionTestUtils.setField(categoryResource, "categorySearchRepository", categorySearchRepository);
        ReflectionTestUtils.setField(categoryResource, "categoryRepository", categoryRepository);
        this.restCategoryMockMvc = MockMvcBuilders.standaloneSetup(categoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        category = new Category();
        category.setName(DEFAULT_NAME);
        category.setDescription(DEFAULT_DESCRIPTION);
        category.setImageUrl(DEFAULT_IMAGE_URL);
        category.setActive(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // Create the Category

        restCategoryMockMvc.perform(post("/api/categorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(category)))
                .andExpect(status().isCreated());

        // Validate the Category in the database
        List<Category> categorys = categoryRepository.findAll();
        assertThat(categorys).hasSize(databaseSizeBeforeCreate + 1);
        Category testCategory = categorys.get(categorys.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCategory.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testCategory.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setName(null);

        // Create the Category, which fails.

        restCategoryMockMvc.perform(post("/api/categorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(category)))
                .andExpect(status().isBadRequest());

        List<Category> categorys = categoryRepository.findAll();
        assertThat(categorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setActive(null);

        // Create the Category, which fails.

        restCategoryMockMvc.perform(post("/api/categorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(category)))
                .andExpect(status().isBadRequest());

        List<Category> categorys = categoryRepository.findAll();
        assertThat(categorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategorys() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categorys
        restCategoryMockMvc.perform(get("/api/categorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get the category
        restCategoryMockMvc.perform(get("/api/categorys/{id}", category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(category.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get("/api/categorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

		int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category
        category.setName(UPDATED_NAME);
        category.setDescription(UPDATED_DESCRIPTION);
        category.setImageUrl(UPDATED_IMAGE_URL);
        category.setActive(UPDATED_ACTIVE);

        restCategoryMockMvc.perform(put("/api/categorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(category)))
                .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categorys = categoryRepository.findAll();
        assertThat(categorys).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categorys.get(categorys.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCategory.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testCategory.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void deleteCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

		int databaseSizeBeforeDelete = categoryRepository.findAll().size();

        // Get the category
        restCategoryMockMvc.perform(delete("/api/categorys/{id}", category.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Category> categorys = categoryRepository.findAll();
        assertThat(categorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
