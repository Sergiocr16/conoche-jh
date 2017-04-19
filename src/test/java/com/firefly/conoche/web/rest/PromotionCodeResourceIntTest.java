package com.firefly.conoche.web.rest;

import com.firefly.conoche.ConocheApp;

import com.firefly.conoche.domain.PromotionCode;
import com.firefly.conoche.repository.PromotionCodeRepository;
import com.firefly.conoche.service.PromotionCodeService;
import com.firefly.conoche.service.PromotionService;
import com.firefly.conoche.service.dto.PromotionCodeDTO;
import com.firefly.conoche.service.mapper.PromotionCodeMapper;
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
 * Test class for the PromotionCodeResource REST controller.
 *
 * @see PromotionCodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConocheApp.class)
public class PromotionCodeResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private PromotionCodeRepository promotionCodeRepository;

    @Autowired
    private PromotionCodeMapper promotionCodeMapper;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private EntityManager em;

    @Autowired
    private PromotionCodeService promotionCodeService;

    private MockMvc restPromotionCodeMockMvc;

    private PromotionCode promotionCode;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PromotionCodeResource promotionCodeResource = new PromotionCodeResource(promotionCodeService,promotionService);
        this.restPromotionCodeMockMvc = MockMvcBuilders.standaloneSetup(promotionCodeResource)
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
    public static PromotionCode createEntity(EntityManager em) {
        PromotionCode promotionCode = new PromotionCode()
                .code(DEFAULT_CODE)
                .active(DEFAULT_ACTIVE);
        return promotionCode;
    }

    @Before
    public void initTest() {
        promotionCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createPromotionCode() throws Exception {
        int databaseSizeBeforeCreate = promotionCodeRepository.findAll().size();

        // Create the PromotionCode
        PromotionCodeDTO promotionCodeDTO = promotionCodeMapper.promotionCodeToPromotionCodeDTO(promotionCode);

        restPromotionCodeMockMvc.perform(post("/api/promotion-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionCodeDTO)))
            .andExpect(status().isCreated());

        // Validate the PromotionCode in the database
        List<PromotionCode> promotionCodeList = promotionCodeRepository.findAll();
        assertThat(promotionCodeList).hasSize(databaseSizeBeforeCreate + 1);
        PromotionCode testPromotionCode = promotionCodeList.get(promotionCodeList.size() - 1);
        assertThat(testPromotionCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPromotionCode.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createPromotionCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = promotionCodeRepository.findAll().size();

        // Create the PromotionCode with an existing ID
        PromotionCode existingPromotionCode = new PromotionCode();
        existingPromotionCode.setId(1L);
        PromotionCodeDTO existingPromotionCodeDTO = promotionCodeMapper.promotionCodeToPromotionCodeDTO(existingPromotionCode);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromotionCodeMockMvc.perform(post("/api/promotion-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPromotionCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PromotionCode> promotionCodeList = promotionCodeRepository.findAll();
        assertThat(promotionCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionCodeRepository.findAll().size();
        // set the field null
        promotionCode.setCode(null);

        // Create the PromotionCode, which fails.
        PromotionCodeDTO promotionCodeDTO = promotionCodeMapper.promotionCodeToPromotionCodeDTO(promotionCode);

        restPromotionCodeMockMvc.perform(post("/api/promotion-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionCodeDTO)))
            .andExpect(status().isBadRequest());

        List<PromotionCode> promotionCodeList = promotionCodeRepository.findAll();
        assertThat(promotionCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionCodeRepository.findAll().size();
        // set the field null
        promotionCode.setActive(null);

        // Create the PromotionCode, which fails.
        PromotionCodeDTO promotionCodeDTO = promotionCodeMapper.promotionCodeToPromotionCodeDTO(promotionCode);

        restPromotionCodeMockMvc.perform(post("/api/promotion-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionCodeDTO)))
            .andExpect(status().isBadRequest());

        List<PromotionCode> promotionCodeList = promotionCodeRepository.findAll();
        assertThat(promotionCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPromotionCodes() throws Exception {
        // Initialize the database
        promotionCodeRepository.saveAndFlush(promotionCode);

        // Get all the promotionCodeList
        restPromotionCodeMockMvc.perform(get("/api/promotion-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotionCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getPromotionCode() throws Exception {
        // Initialize the database
        promotionCodeRepository.saveAndFlush(promotionCode);

        // Get the promotionCode
        restPromotionCodeMockMvc.perform(get("/api/promotion-codes/{id}", promotionCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(promotionCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPromotionCode() throws Exception {
        // Get the promotionCode
        restPromotionCodeMockMvc.perform(get("/api/promotion-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePromotionCode() throws Exception {
        // Initialize the database
        promotionCodeRepository.saveAndFlush(promotionCode);
        int databaseSizeBeforeUpdate = promotionCodeRepository.findAll().size();

        // Update the promotionCode
        PromotionCode updatedPromotionCode = promotionCodeRepository.findOne(promotionCode.getId());
        updatedPromotionCode
                .code(UPDATED_CODE)
                .active(UPDATED_ACTIVE);
        PromotionCodeDTO promotionCodeDTO = promotionCodeMapper.promotionCodeToPromotionCodeDTO(updatedPromotionCode);

        restPromotionCodeMockMvc.perform(put("/api/promotion-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionCodeDTO)))
            .andExpect(status().isOk());

        // Validate the PromotionCode in the database
        List<PromotionCode> promotionCodeList = promotionCodeRepository.findAll();
        assertThat(promotionCodeList).hasSize(databaseSizeBeforeUpdate);
        PromotionCode testPromotionCode = promotionCodeList.get(promotionCodeList.size() - 1);
        assertThat(testPromotionCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPromotionCode.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingPromotionCode() throws Exception {
        int databaseSizeBeforeUpdate = promotionCodeRepository.findAll().size();

        // Create the PromotionCode
        PromotionCodeDTO promotionCodeDTO = promotionCodeMapper.promotionCodeToPromotionCodeDTO(promotionCode);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPromotionCodeMockMvc.perform(put("/api/promotion-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionCodeDTO)))
            .andExpect(status().isCreated());

        // Validate the PromotionCode in the database
        List<PromotionCode> promotionCodeList = promotionCodeRepository.findAll();
        assertThat(promotionCodeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePromotionCode() throws Exception {
        // Initialize the database
        promotionCodeRepository.saveAndFlush(promotionCode);
        int databaseSizeBeforeDelete = promotionCodeRepository.findAll().size();

        // Get the promotionCode
        restPromotionCodeMockMvc.perform(delete("/api/promotion-codes/{id}", promotionCode.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PromotionCode> promotionCodeList = promotionCodeRepository.findAll();
        assertThat(promotionCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromotionCode.class);
    }
}
