package com.firefly.conoche.web.rest;

import com.firefly.conoche.ConocheApp;

import com.firefly.conoche.domain.ObjectChange;
import com.firefly.conoche.repository.ObjectChangeRepository;
import com.firefly.conoche.service.ObjectChangeService;
import com.firefly.conoche.service.dto.ObjectChangeDTO;
import com.firefly.conoche.service.mapper.ObjectChangeMapper;
import com.firefly.conoche.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ObjectChangeResource REST controller.
 *
 * @see ObjectChangeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConocheApp.class)
public class ObjectChangeResourceIntTest {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    @Autowired
    private ObjectChangeRepository objectChangeRepository;

    @Autowired
    private ObjectChangeMapper objectChangeMapper;

    @Autowired
    private ObjectChangeService objectChangeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restObjectChangeMockMvc;

    private ObjectChange objectChange;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ObjectChangeResource objectChangeResource = new ObjectChangeResource(objectChangeService);
        this.restObjectChangeMockMvc = MockMvcBuilders.standaloneSetup(objectChangeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ObjectChange createEntity(EntityManager em) {
        ObjectChange objectChange = new ObjectChange()
                .fieldName(DEFAULT_FIELD_NAME);
        return objectChange;
    }

    @Before
    public void initTest() {
        objectChange = createEntity(em);
    }

    @Test
    @Transactional
    public void createObjectChange() throws Exception {
        int databaseSizeBeforeCreate = objectChangeRepository.findAll().size();

        // Create the ObjectChange
        ObjectChangeDTO objectChangeDTO = objectChangeMapper.objectChangeToObjectChangeDTO(objectChange);

        restObjectChangeMockMvc.perform(post("/api/object-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(objectChangeDTO)))
            .andExpect(status().isCreated());

        // Validate the ObjectChange in the database
        List<ObjectChange> objectChangeList = objectChangeRepository.findAll();
        assertThat(objectChangeList).hasSize(databaseSizeBeforeCreate + 1);
        ObjectChange testObjectChange = objectChangeList.get(objectChangeList.size() - 1);
        assertThat(testObjectChange.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    @Transactional
    public void createObjectChangeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = objectChangeRepository.findAll().size();

        // Create the ObjectChange with an existing ID
        ObjectChange existingObjectChange = new ObjectChange();
        existingObjectChange.setId(1L);
        ObjectChangeDTO existingObjectChangeDTO = objectChangeMapper.objectChangeToObjectChangeDTO(existingObjectChange);

        // An entity with an existing ID cannot be created, so this API call must fail
        restObjectChangeMockMvc.perform(post("/api/object-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingObjectChangeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ObjectChange> objectChangeList = objectChangeRepository.findAll();
        assertThat(objectChangeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFieldNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = objectChangeRepository.findAll().size();
        // set the field null
        objectChange.setFieldName(null);

        // Create the ObjectChange, which fails.
        ObjectChangeDTO objectChangeDTO = objectChangeMapper.objectChangeToObjectChangeDTO(objectChange);

        restObjectChangeMockMvc.perform(post("/api/object-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(objectChangeDTO)))
            .andExpect(status().isBadRequest());

        List<ObjectChange> objectChangeList = objectChangeRepository.findAll();
        assertThat(objectChangeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllObjectChanges() throws Exception {
        // Initialize the database
        objectChangeRepository.saveAndFlush(objectChange);

        // Get all the objectChangeList
        restObjectChangeMockMvc.perform(get("/api/object-changes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objectChange.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME.toString())));
    }

    @Test
    @Transactional
    public void getObjectChange() throws Exception {
        // Initialize the database
        objectChangeRepository.saveAndFlush(objectChange);

        // Get the objectChange
        restObjectChangeMockMvc.perform(get("/api/object-changes/{id}", objectChange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(objectChange.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingObjectChange() throws Exception {
        // Get the objectChange
        restObjectChangeMockMvc.perform(get("/api/object-changes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObjectChange() throws Exception {
        // Initialize the database
        objectChangeRepository.saveAndFlush(objectChange);
        int databaseSizeBeforeUpdate = objectChangeRepository.findAll().size();

        // Update the objectChange
        ObjectChange updatedObjectChange = objectChangeRepository.findOne(objectChange.getId());
        updatedObjectChange
                .fieldName(UPDATED_FIELD_NAME);
        ObjectChangeDTO objectChangeDTO = objectChangeMapper.objectChangeToObjectChangeDTO(updatedObjectChange);

        restObjectChangeMockMvc.perform(put("/api/object-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(objectChangeDTO)))
            .andExpect(status().isOk());

        // Validate the ObjectChange in the database
        List<ObjectChange> objectChangeList = objectChangeRepository.findAll();
        assertThat(objectChangeList).hasSize(databaseSizeBeforeUpdate);
        ObjectChange testObjectChange = objectChangeList.get(objectChangeList.size() - 1);
        assertThat(testObjectChange.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingObjectChange() throws Exception {
        int databaseSizeBeforeUpdate = objectChangeRepository.findAll().size();

        // Create the ObjectChange
        ObjectChangeDTO objectChangeDTO = objectChangeMapper.objectChangeToObjectChangeDTO(objectChange);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restObjectChangeMockMvc.perform(put("/api/object-changes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(objectChangeDTO)))
            .andExpect(status().isCreated());

        // Validate the ObjectChange in the database
        List<ObjectChange> objectChangeList = objectChangeRepository.findAll();
        assertThat(objectChangeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteObjectChange() throws Exception {
        // Initialize the database
        objectChangeRepository.saveAndFlush(objectChange);
        int databaseSizeBeforeDelete = objectChangeRepository.findAll().size();

        // Get the objectChange
        restObjectChangeMockMvc.perform(delete("/api/object-changes/{id}", objectChange.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ObjectChange> objectChangeList = objectChangeRepository.findAll();
        assertThat(objectChangeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ObjectChange.class);
    }
}
