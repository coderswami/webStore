package com.tl.webstore.web.rest;

import com.tl.webstore.Application;
import com.tl.webstore.domain.Catalog;
import com.tl.webstore.repository.CatalogRepository;
import com.tl.webstore.repository.search.CatalogSearchRepository;

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
 * Test class for the CatalogResource REST controller.
 *
 * @see CatalogResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CatalogResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private CatalogRepository catalogRepository;

    @Inject
    private CatalogSearchRepository catalogSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCatalogMockMvc;

    private Catalog catalog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CatalogResource catalogResource = new CatalogResource();
        ReflectionTestUtils.setField(catalogResource, "catalogSearchRepository", catalogSearchRepository);
        ReflectionTestUtils.setField(catalogResource, "catalogRepository", catalogRepository);
        this.restCatalogMockMvc = MockMvcBuilders.standaloneSetup(catalogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        catalog = new Catalog();
        catalog.setName(DEFAULT_NAME);
        catalog.setDescription(DEFAULT_DESCRIPTION);
        catalog.setActive(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createCatalog() throws Exception {
        int databaseSizeBeforeCreate = catalogRepository.findAll().size();

        // Create the Catalog

        restCatalogMockMvc.perform(post("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isCreated());

        // Validate the Catalog in the database
        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeCreate + 1);
        Catalog testCatalog = catalogs.get(catalogs.size() - 1);
        assertThat(testCatalog.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalog.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatalog.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogRepository.findAll().size();
        // set the field null
        catalog.setName(null);

        // Create the Catalog, which fails.

        restCatalogMockMvc.perform(post("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isBadRequest());

        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogRepository.findAll().size();
        // set the field null
        catalog.setDescription(null);

        // Create the Catalog, which fails.

        restCatalogMockMvc.perform(post("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isBadRequest());

        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogRepository.findAll().size();
        // set the field null
        catalog.setActive(null);

        // Create the Catalog, which fails.

        restCatalogMockMvc.perform(post("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isBadRequest());

        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCatalogs() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        // Get all the catalogs
        restCatalogMockMvc.perform(get("/api/catalogs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(catalog.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        // Get the catalog
        restCatalogMockMvc.perform(get("/api/catalogs/{id}", catalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(catalog.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCatalog() throws Exception {
        // Get the catalog
        restCatalogMockMvc.perform(get("/api/catalogs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

		int databaseSizeBeforeUpdate = catalogRepository.findAll().size();

        // Update the catalog
        catalog.setName(UPDATED_NAME);
        catalog.setDescription(UPDATED_DESCRIPTION);
        catalog.setActive(UPDATED_ACTIVE);

        restCatalogMockMvc.perform(put("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isOk());

        // Validate the Catalog in the database
        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeUpdate);
        Catalog testCatalog = catalogs.get(catalogs.size() - 1);
        assertThat(testCatalog.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalog.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void deleteCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

		int databaseSizeBeforeDelete = catalogRepository.findAll().size();

        // Get the catalog
        restCatalogMockMvc.perform(delete("/api/catalogs/{id}", catalog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
