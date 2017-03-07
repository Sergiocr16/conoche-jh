package com.firefly.conoche.web.rest;

import com.firefly.conoche.ConocheApp;

import com.firefly.conoche.domain.Promotion;
import com.firefly.conoche.repository.PromotionRepository;
import com.firefly.conoche.service.PromotionService;
import com.firefly.conoche.service.dto.PromotionDTO;
import com.firefly.conoche.service.mapper.PromotionMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.firefly.conoche.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PromotionResource REST controller.
 *
 * @see PromotionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConocheApp.class)
public class PromotionResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_INITIAL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INITIAL_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FINAL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FINAL_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_MAXIMUM_CODE_PER_USER = 1;
    private static final Integer UPDATED_MAXIMUM_CODE_PER_USER = 2;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionMapper promotionMapper;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPromotionMockMvc;

    private Promotion promotion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PromotionResource promotionResource = new PromotionResource(promotionService);
        this.restPromotionMockMvc = MockMvcBuilders.standaloneSetup(promotionResource)
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
    public static Promotion createEntity(EntityManager em) {
        Promotion promotion = new Promotion()
                .description(DEFAULT_DESCRIPTION)
                .imageUrl(DEFAULT_IMAGE_URL)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
                .initialTime(DEFAULT_INITIAL_TIME)
                .finalTime(DEFAULT_FINAL_TIME)
                .maximumCodePerUser(DEFAULT_MAXIMUM_CODE_PER_USER);
        return promotion;
    }

    @Before
    public void initTest() {
        promotion = createEntity(em);
    }

    @Test
    @Transactional
    public void createPromotion() throws Exception {
        int databaseSizeBeforeCreate = promotionRepository.findAll().size();

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.promotionToPromotionDTO(promotion);

        restPromotionMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isCreated());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeCreate + 1);
        Promotion testPromotion = promotionList.get(promotionList.size() - 1);
        assertThat(testPromotion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPromotion.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testPromotion.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPromotion.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testPromotion.getInitialTime()).isEqualTo(DEFAULT_INITIAL_TIME);
        assertThat(testPromotion.getFinalTime()).isEqualTo(DEFAULT_FINAL_TIME);
        assertThat(testPromotion.getMaximumCodePerUser()).isEqualTo(DEFAULT_MAXIMUM_CODE_PER_USER);
    }

    @Test
    @Transactional
    public void createPromotionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = promotionRepository.findAll().size();

        // Create the Promotion with an existing ID
        Promotion existingPromotion = new Promotion();
        existingPromotion.setId(1L);
        PromotionDTO existingPromotionDTO = promotionMapper.promotionToPromotionDTO(existingPromotion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromotionMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPromotionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setDescription(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.promotionToPromotionDTO(promotion);

        restPromotionMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInitialTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setInitialTime(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.promotionToPromotionDTO(promotion);

        restPromotionMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinalTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setFinalTime(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.promotionToPromotionDTO(promotion);

        restPromotionMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaximumCodePerUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setMaximumCodePerUser(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.promotionToPromotionDTO(promotion);

        restPromotionMockMvc.perform(post("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPromotions() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList
        restPromotionMockMvc.perform(get("/api/promotions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotion.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].initialTime").value(hasItem(sameInstant(DEFAULT_INITIAL_TIME))))
            .andExpect(jsonPath("$.[*].finalTime").value(hasItem(sameInstant(DEFAULT_FINAL_TIME))))
            .andExpect(jsonPath("$.[*].maximumCodePerUser").value(hasItem(DEFAULT_MAXIMUM_CODE_PER_USER)));
    }

    @Test
    @Transactional
    public void getPromotion() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get the promotion
        restPromotionMockMvc.perform(get("/api/promotions/{id}", promotion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(promotion.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.initialTime").value(sameInstant(DEFAULT_INITIAL_TIME)))
            .andExpect(jsonPath("$.finalTime").value(sameInstant(DEFAULT_FINAL_TIME)))
            .andExpect(jsonPath("$.maximumCodePerUser").value(DEFAULT_MAXIMUM_CODE_PER_USER));
    }

    @Test
    @Transactional
    public void getNonExistingPromotion() throws Exception {
        // Get the promotion
        restPromotionMockMvc.perform(get("/api/promotions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePromotion() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);
        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();

        // Update the promotion
        Promotion updatedPromotion = promotionRepository.findOne(promotion.getId());
        updatedPromotion
                .description(UPDATED_DESCRIPTION)
                .imageUrl(UPDATED_IMAGE_URL)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
                .initialTime(UPDATED_INITIAL_TIME)
                .finalTime(UPDATED_FINAL_TIME)
                .maximumCodePerUser(UPDATED_MAXIMUM_CODE_PER_USER);
        PromotionDTO promotionDTO = promotionMapper.promotionToPromotionDTO(updatedPromotion);

        restPromotionMockMvc.perform(put("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isOk());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
        Promotion testPromotion = promotionList.get(promotionList.size() - 1);
        assertThat(testPromotion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPromotion.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testPromotion.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPromotion.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testPromotion.getInitialTime()).isEqualTo(UPDATED_INITIAL_TIME);
        assertThat(testPromotion.getFinalTime()).isEqualTo(UPDATED_FINAL_TIME);
        assertThat(testPromotion.getMaximumCodePerUser()).isEqualTo(UPDATED_MAXIMUM_CODE_PER_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.promotionToPromotionDTO(promotion);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPromotionMockMvc.perform(put("/api/promotions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isCreated());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePromotion() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);
        int databaseSizeBeforeDelete = promotionRepository.findAll().size();

        // Get the promotion
        restPromotionMockMvc.perform(delete("/api/promotions/{id}", promotion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Promotion.class);
    }
}
