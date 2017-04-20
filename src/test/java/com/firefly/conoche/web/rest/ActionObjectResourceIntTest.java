package com.firefly.conoche.web.rest;

import com.firefly.conoche.ConocheApp;

import com.firefly.conoche.domain.ActionObject;
import com.firefly.conoche.repository.ActionObjectRepository;
import com.firefly.conoche.service.ActionObjectService;
import com.firefly.conoche.service.dto.ActionObjectDTO;
import com.firefly.conoche.service.mapper.ActionObjectMapper;
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

import com.firefly.conoche.domain.enumeration.ActionType;
import com.firefly.conoche.domain.enumeration.ActionObjectType;
/**
 * Test class for the ActionObjectResource REST controller.
 *
 * @see ActionObjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConocheApp.class)
public class ActionObjectResourceIntTest {

    private static final Long DEFAULT_OBJECT_ID = 1L;
    private static final Long UPDATED_OBJECT_ID = 2L;

    private static final ActionType DEFAULT_ACTION_TYPE = ActionType.CREATE;
    private static final ActionType UPDATED_ACTION_TYPE = ActionType.UPDATE;

    private static final ActionObjectType DEFAULT_OBJECT_TYPE = ActionObjectType.USER;
    private static final ActionObjectType UPDATED_OBJECT_TYPE = ActionObjectType.LOCAL;

    @Autowired
    private ActionObjectRepository actionObjectRepository;

    @Autowired
    private ActionObjectMapper actionObjectMapper;

    @Autowired
    private ActionObjectService actionObjectService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActionObjectMockMvc;

    private ActionObject actionObject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActionObjectResource actionObjectResource = new ActionObjectResource(actionObjectService);
        this.restActionObjectMockMvc = MockMvcBuilders.standaloneSetup(actionObjectResource)
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
    public static ActionObject createEntity(EntityManager em) {
        ActionObject actionObject = new ActionObject()
                .objectId(DEFAULT_OBJECT_ID)
                .actionType(DEFAULT_ACTION_TYPE)
                .objectType(DEFAULT_OBJECT_TYPE);
        return actionObject;
    }

    @Before
    public void initTest() {
        actionObject = createEntity(em);
    }

    @Test
    @Transactional
    public void createActionObject() throws Exception {
        int databaseSizeBeforeCreate = actionObjectRepository.findAll().size();

        // Create the ActionObject
        ActionObjectDTO actionObjectDTO = actionObjectMapper.actionObjectToActionObjectDTO(actionObject);

        restActionObjectMockMvc.perform(post("/api/action-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionObjectDTO)))
            .andExpect(status().isCreated());

        // Validate the ActionObject in the database
        List<ActionObject> actionObjectList = actionObjectRepository.findAll();
        assertThat(actionObjectList).hasSize(databaseSizeBeforeCreate + 1);
        ActionObject testActionObject = actionObjectList.get(actionObjectList.size() - 1);
        assertThat(testActionObject.getObjectId()).isEqualTo(DEFAULT_OBJECT_ID);
        assertThat(testActionObject.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
        assertThat(testActionObject.getObjectType()).isEqualTo(DEFAULT_OBJECT_TYPE);
    }

    @Test
    @Transactional
    public void createActionObjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actionObjectRepository.findAll().size();

        // Create the ActionObject with an existing ID
        ActionObject existingActionObject = new ActionObject();
        existingActionObject.setId(1L);
        ActionObjectDTO existingActionObjectDTO = actionObjectMapper.actionObjectToActionObjectDTO(existingActionObject);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActionObjectMockMvc.perform(post("/api/action-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingActionObjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ActionObject> actionObjectList = actionObjectRepository.findAll();
        assertThat(actionObjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkObjectIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = actionObjectRepository.findAll().size();
        // set the field null
        actionObject.setObjectId(null);

        // Create the ActionObject, which fails.
        ActionObjectDTO actionObjectDTO = actionObjectMapper.actionObjectToActionObjectDTO(actionObject);

        restActionObjectMockMvc.perform(post("/api/action-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionObjectDTO)))
            .andExpect(status().isBadRequest());

        List<ActionObject> actionObjectList = actionObjectRepository.findAll();
        assertThat(actionObjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActionObjects() throws Exception {
        // Initialize the database
        actionObjectRepository.saveAndFlush(actionObject);

        // Get all the actionObjectList
        restActionObjectMockMvc.perform(get("/api/action-objects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actionObject.getId().intValue())))
            .andExpect(jsonPath("$.[*].objectId").value(hasItem(DEFAULT_OBJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objectType").value(hasItem(DEFAULT_OBJECT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getActionObject() throws Exception {
        // Initialize the database
        actionObjectRepository.saveAndFlush(actionObject);

        // Get the actionObject
        restActionObjectMockMvc.perform(get("/api/action-objects/{id}", actionObject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actionObject.getId().intValue()))
            .andExpect(jsonPath("$.objectId").value(DEFAULT_OBJECT_ID.intValue()))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()))
            .andExpect(jsonPath("$.objectType").value(DEFAULT_OBJECT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActionObject() throws Exception {
        // Get the actionObject
        restActionObjectMockMvc.perform(get("/api/action-objects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActionObject() throws Exception {
        // Initialize the database
        actionObjectRepository.saveAndFlush(actionObject);
        int databaseSizeBeforeUpdate = actionObjectRepository.findAll().size();

        // Update the actionObject
        ActionObject updatedActionObject = actionObjectRepository.findOne(actionObject.getId());
        updatedActionObject
                .objectId(UPDATED_OBJECT_ID)
                .actionType(UPDATED_ACTION_TYPE)
                .objectType(UPDATED_OBJECT_TYPE);
        ActionObjectDTO actionObjectDTO = actionObjectMapper.actionObjectToActionObjectDTO(updatedActionObject);

        restActionObjectMockMvc.perform(put("/api/action-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionObjectDTO)))
            .andExpect(status().isOk());

        // Validate the ActionObject in the database
        List<ActionObject> actionObjectList = actionObjectRepository.findAll();
        assertThat(actionObjectList).hasSize(databaseSizeBeforeUpdate);
        ActionObject testActionObject = actionObjectList.get(actionObjectList.size() - 1);
        assertThat(testActionObject.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testActionObject.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
        assertThat(testActionObject.getObjectType()).isEqualTo(UPDATED_OBJECT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingActionObject() throws Exception {
        int databaseSizeBeforeUpdate = actionObjectRepository.findAll().size();

        // Create the ActionObject
        ActionObjectDTO actionObjectDTO = actionObjectMapper.actionObjectToActionObjectDTO(actionObject);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActionObjectMockMvc.perform(put("/api/action-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actionObjectDTO)))
            .andExpect(status().isCreated());

        // Validate the ActionObject in the database
        List<ActionObject> actionObjectList = actionObjectRepository.findAll();
        assertThat(actionObjectList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteActionObject() throws Exception {
        // Initialize the database
        actionObjectRepository.saveAndFlush(actionObject);
        int databaseSizeBeforeDelete = actionObjectRepository.findAll().size();

        // Get the actionObject
        restActionObjectMockMvc.perform(delete("/api/action-objects/{id}", actionObject.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActionObject> actionObjectList = actionObjectRepository.findAll();
        assertThat(actionObjectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActionObject.class);
    }
}
