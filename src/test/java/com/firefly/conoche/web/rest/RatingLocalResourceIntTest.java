package com.firefly.conoche.web.rest;

import com.firefly.conoche.ConocheApp;

import com.firefly.conoche.domain.RatingLocal;
import com.firefly.conoche.repository.RatingLocalRepository;
import com.firefly.conoche.service.RatingLocalService;
import com.firefly.conoche.service.dto.RatingLocalDTO;
import com.firefly.conoche.service.mapper.RatingLocalMapper;
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
 * Test class for the RatingLocalResource REST controller.
 *
 * @see RatingLocalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConocheApp.class)
public class RatingLocalResourceIntTest {

    private static final Integer DEFAULT_RATING = 0;
    private static final Integer UPDATED_RATING = 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RatingLocalRepository ratingLocalRepository;

    @Autowired
    private RatingLocalMapper ratingLocalMapper;

    @Autowired
    private RatingLocalService ratingLocalService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRatingLocalMockMvc;

    private RatingLocal ratingLocal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RatingLocalResource ratingLocalResource = new RatingLocalResource(ratingLocalService);
        this.restRatingLocalMockMvc = MockMvcBuilders.standaloneSetup(ratingLocalResource)
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
    public static RatingLocal createEntity(EntityManager em) {
        RatingLocal ratingLocal = new RatingLocal()
                .rating(DEFAULT_RATING)
                .description(DEFAULT_DESCRIPTION);
        return ratingLocal;
    }

    @Before
    public void initTest() {
        ratingLocal = createEntity(em);
    }

    @Test
    @Transactional
    public void createRatingLocal() throws Exception {
        int databaseSizeBeforeCreate = ratingLocalRepository.findAll().size();

        // Create the RatingLocal
        RatingLocalDTO ratingLocalDTO = ratingLocalMapper.ratingLocalToRatingLocalDTO(ratingLocal);

        restRatingLocalMockMvc.perform(post("/api/rating-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingLocalDTO)))
            .andExpect(status().isCreated());

        // Validate the RatingLocal in the database
        List<RatingLocal> ratingLocalList = ratingLocalRepository.findAll();
        assertThat(ratingLocalList).hasSize(databaseSizeBeforeCreate + 1);
        RatingLocal testRatingLocal = ratingLocalList.get(ratingLocalList.size() - 1);
        assertThat(testRatingLocal.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testRatingLocal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRatingLocalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ratingLocalRepository.findAll().size();

        // Create the RatingLocal with an existing ID
        RatingLocal existingRatingLocal = new RatingLocal();
        existingRatingLocal.setId(1L);
        RatingLocalDTO existingRatingLocalDTO = ratingLocalMapper.ratingLocalToRatingLocalDTO(existingRatingLocal);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingLocalMockMvc.perform(post("/api/rating-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingRatingLocalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RatingLocal> ratingLocalList = ratingLocalRepository.findAll();
        assertThat(ratingLocalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingLocalRepository.findAll().size();
        // set the field null
        ratingLocal.setRating(null);

        // Create the RatingLocal, which fails.
        RatingLocalDTO ratingLocalDTO = ratingLocalMapper.ratingLocalToRatingLocalDTO(ratingLocal);

        restRatingLocalMockMvc.perform(post("/api/rating-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingLocalDTO)))
            .andExpect(status().isBadRequest());

        List<RatingLocal> ratingLocalList = ratingLocalRepository.findAll();
        assertThat(ratingLocalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRatingLocals() throws Exception {
        // Initialize the database
        ratingLocalRepository.saveAndFlush(ratingLocal);

        // Get all the ratingLocalList
        restRatingLocalMockMvc.perform(get("/api/rating-locals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ratingLocal.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getRatingLocal() throws Exception {
        // Initialize the database
        ratingLocalRepository.saveAndFlush(ratingLocal);

        // Get the ratingLocal
        restRatingLocalMockMvc.perform(get("/api/rating-locals/{id}", ratingLocal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ratingLocal.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRatingLocal() throws Exception {
        // Get the ratingLocal
        restRatingLocalMockMvc.perform(get("/api/rating-locals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRatingLocal() throws Exception {
        // Initialize the database
        ratingLocalRepository.saveAndFlush(ratingLocal);
        int databaseSizeBeforeUpdate = ratingLocalRepository.findAll().size();

        // Update the ratingLocal
        RatingLocal updatedRatingLocal = ratingLocalRepository.findOne(ratingLocal.getId());
        updatedRatingLocal
                .rating(UPDATED_RATING)
                .description(UPDATED_DESCRIPTION);
        RatingLocalDTO ratingLocalDTO = ratingLocalMapper.ratingLocalToRatingLocalDTO(updatedRatingLocal);

        restRatingLocalMockMvc.perform(put("/api/rating-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingLocalDTO)))
            .andExpect(status().isOk());

        // Validate the RatingLocal in the database
        List<RatingLocal> ratingLocalList = ratingLocalRepository.findAll();
        assertThat(ratingLocalList).hasSize(databaseSizeBeforeUpdate);
        RatingLocal testRatingLocal = ratingLocalList.get(ratingLocalList.size() - 1);
        assertThat(testRatingLocal.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testRatingLocal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRatingLocal() throws Exception {
        int databaseSizeBeforeUpdate = ratingLocalRepository.findAll().size();

        // Create the RatingLocal
        RatingLocalDTO ratingLocalDTO = ratingLocalMapper.ratingLocalToRatingLocalDTO(ratingLocal);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRatingLocalMockMvc.perform(put("/api/rating-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingLocalDTO)))
            .andExpect(status().isCreated());

        // Validate the RatingLocal in the database
        List<RatingLocal> ratingLocalList = ratingLocalRepository.findAll();
        assertThat(ratingLocalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRatingLocal() throws Exception {
        // Initialize the database
        ratingLocalRepository.saveAndFlush(ratingLocal);
        int databaseSizeBeforeDelete = ratingLocalRepository.findAll().size();

        // Get the ratingLocal
        restRatingLocalMockMvc.perform(delete("/api/rating-locals/{id}", ratingLocal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RatingLocal> ratingLocalList = ratingLocalRepository.findAll();
        assertThat(ratingLocalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RatingLocal.class);
    }
}
