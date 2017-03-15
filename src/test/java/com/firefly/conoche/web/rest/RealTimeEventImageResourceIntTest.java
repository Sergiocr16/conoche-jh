package com.firefly.conoche.web.rest;

import com.firefly.conoche.ConocheApp;

import com.firefly.conoche.domain.RealTimeEventImage;
import com.firefly.conoche.repository.RealTimeEventImageRepository;
import com.firefly.conoche.service.RealTimeEventImageService;
import com.firefly.conoche.service.dto.RealTimeEventImageDTO;
import com.firefly.conoche.service.mapper.RealTimeEventImageMapper;
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
 * Test class for the RealTimeEventImageResource REST controller.
 *
 * @see RealTimeEventImageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConocheApp.class)
public class RealTimeEventImageResourceIntTest {

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RealTimeEventImageRepository realTimeEventImageRepository;

    @Autowired
    private RealTimeEventImageMapper realTimeEventImageMapper;

    @Autowired
    private RealTimeEventImageService realTimeEventImageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRealTimeEventImageMockMvc;

    private RealTimeEventImage realTimeEventImage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RealTimeEventImageResource realTimeEventImageResource = new RealTimeEventImageResource(realTimeEventImageService);
        this.restRealTimeEventImageMockMvc = MockMvcBuilders.standaloneSetup(realTimeEventImageResource)
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
    public static RealTimeEventImage createEntity(EntityManager em) {
        RealTimeEventImage realTimeEventImage = new RealTimeEventImage()
                .imageUrl(DEFAULT_IMAGE_URL)
                .creationTime(DEFAULT_CREATION_TIME)
                .width(DEFAULT_WIDTH)
                .height(DEFAULT_HEIGHT)
                .description(DEFAULT_DESCRIPTION);
        return realTimeEventImage;
    }

    @Before
    public void initTest() {
        realTimeEventImage = createEntity(em);
    }

    @Test
    @Transactional
    public void createRealTimeEventImage() throws Exception {
        int databaseSizeBeforeCreate = realTimeEventImageRepository.findAll().size();

        // Create the RealTimeEventImage
        RealTimeEventImageDTO realTimeEventImageDTO = realTimeEventImageMapper.realTimeEventImageToRealTimeEventImageDTO(realTimeEventImage);

        restRealTimeEventImageMockMvc.perform(post("/api/real-time-event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(realTimeEventImageDTO)))
            .andExpect(status().isCreated());

        // Validate the RealTimeEventImage in the database
        List<RealTimeEventImage> realTimeEventImageList = realTimeEventImageRepository.findAll();
        assertThat(realTimeEventImageList).hasSize(databaseSizeBeforeCreate + 1);
        RealTimeEventImage testRealTimeEventImage = realTimeEventImageList.get(realTimeEventImageList.size() - 1);
        assertThat(testRealTimeEventImage.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testRealTimeEventImage.getCreationTime()).isEqualTo(DEFAULT_CREATION_TIME);
        assertThat(testRealTimeEventImage.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testRealTimeEventImage.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testRealTimeEventImage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRealTimeEventImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = realTimeEventImageRepository.findAll().size();

        // Create the RealTimeEventImage with an existing ID
        RealTimeEventImage existingRealTimeEventImage = new RealTimeEventImage();
        existingRealTimeEventImage.setId(1L);
        RealTimeEventImageDTO existingRealTimeEventImageDTO = realTimeEventImageMapper.realTimeEventImageToRealTimeEventImageDTO(existingRealTimeEventImage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRealTimeEventImageMockMvc.perform(post("/api/real-time-event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingRealTimeEventImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RealTimeEventImage> realTimeEventImageList = realTimeEventImageRepository.findAll();
        assertThat(realTimeEventImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreationTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = realTimeEventImageRepository.findAll().size();
        // set the field null
        realTimeEventImage.setCreationTime(null);

        // Create the RealTimeEventImage, which fails.
        RealTimeEventImageDTO realTimeEventImageDTO = realTimeEventImageMapper.realTimeEventImageToRealTimeEventImageDTO(realTimeEventImage);

        restRealTimeEventImageMockMvc.perform(post("/api/real-time-event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(realTimeEventImageDTO)))
            .andExpect(status().isBadRequest());

        List<RealTimeEventImage> realTimeEventImageList = realTimeEventImageRepository.findAll();
        assertThat(realTimeEventImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWidthIsRequired() throws Exception {
        int databaseSizeBeforeTest = realTimeEventImageRepository.findAll().size();
        // set the field null
        realTimeEventImage.setWidth(null);

        // Create the RealTimeEventImage, which fails.
        RealTimeEventImageDTO realTimeEventImageDTO = realTimeEventImageMapper.realTimeEventImageToRealTimeEventImageDTO(realTimeEventImage);

        restRealTimeEventImageMockMvc.perform(post("/api/real-time-event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(realTimeEventImageDTO)))
            .andExpect(status().isBadRequest());

        List<RealTimeEventImage> realTimeEventImageList = realTimeEventImageRepository.findAll();
        assertThat(realTimeEventImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = realTimeEventImageRepository.findAll().size();
        // set the field null
        realTimeEventImage.setHeight(null);

        // Create the RealTimeEventImage, which fails.
        RealTimeEventImageDTO realTimeEventImageDTO = realTimeEventImageMapper.realTimeEventImageToRealTimeEventImageDTO(realTimeEventImage);

        restRealTimeEventImageMockMvc.perform(post("/api/real-time-event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(realTimeEventImageDTO)))
            .andExpect(status().isBadRequest());

        List<RealTimeEventImage> realTimeEventImageList = realTimeEventImageRepository.findAll();
        assertThat(realTimeEventImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRealTimeEventImages() throws Exception {
        // Initialize the database
        realTimeEventImageRepository.saveAndFlush(realTimeEventImage);

        // Get all the realTimeEventImageList
        restRealTimeEventImageMockMvc.perform(get("/api/real-time-event-images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(realTimeEventImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].creationTime").value(hasItem(sameInstant(DEFAULT_CREATION_TIME))))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getRealTimeEventImage() throws Exception {
        // Initialize the database
        realTimeEventImageRepository.saveAndFlush(realTimeEventImage);

        // Get the realTimeEventImage
        restRealTimeEventImageMockMvc.perform(get("/api/real-time-event-images/{id}", realTimeEventImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(realTimeEventImage.getId().intValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.creationTime").value(sameInstant(DEFAULT_CREATION_TIME)))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRealTimeEventImage() throws Exception {
        // Get the realTimeEventImage
        restRealTimeEventImageMockMvc.perform(get("/api/real-time-event-images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRealTimeEventImage() throws Exception {
        // Initialize the database
        realTimeEventImageRepository.saveAndFlush(realTimeEventImage);
        int databaseSizeBeforeUpdate = realTimeEventImageRepository.findAll().size();

        // Update the realTimeEventImage
        RealTimeEventImage updatedRealTimeEventImage = realTimeEventImageRepository.findOne(realTimeEventImage.getId());
        updatedRealTimeEventImage
                .imageUrl(UPDATED_IMAGE_URL)
                .creationTime(UPDATED_CREATION_TIME)
                .width(UPDATED_WIDTH)
                .height(UPDATED_HEIGHT)
                .description(UPDATED_DESCRIPTION);
        RealTimeEventImageDTO realTimeEventImageDTO = realTimeEventImageMapper.realTimeEventImageToRealTimeEventImageDTO(updatedRealTimeEventImage);

        restRealTimeEventImageMockMvc.perform(put("/api/real-time-event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(realTimeEventImageDTO)))
            .andExpect(status().isOk());

        // Validate the RealTimeEventImage in the database
        List<RealTimeEventImage> realTimeEventImageList = realTimeEventImageRepository.findAll();
        assertThat(realTimeEventImageList).hasSize(databaseSizeBeforeUpdate);
        RealTimeEventImage testRealTimeEventImage = realTimeEventImageList.get(realTimeEventImageList.size() - 1);
        assertThat(testRealTimeEventImage.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testRealTimeEventImage.getCreationTime()).isEqualTo(UPDATED_CREATION_TIME);
        assertThat(testRealTimeEventImage.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testRealTimeEventImage.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testRealTimeEventImage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRealTimeEventImage() throws Exception {
        int databaseSizeBeforeUpdate = realTimeEventImageRepository.findAll().size();

        // Create the RealTimeEventImage
        RealTimeEventImageDTO realTimeEventImageDTO = realTimeEventImageMapper.realTimeEventImageToRealTimeEventImageDTO(realTimeEventImage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRealTimeEventImageMockMvc.perform(put("/api/real-time-event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(realTimeEventImageDTO)))
            .andExpect(status().isCreated());

        // Validate the RealTimeEventImage in the database
        List<RealTimeEventImage> realTimeEventImageList = realTimeEventImageRepository.findAll();
        assertThat(realTimeEventImageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRealTimeEventImage() throws Exception {
        // Initialize the database
        realTimeEventImageRepository.saveAndFlush(realTimeEventImage);
        int databaseSizeBeforeDelete = realTimeEventImageRepository.findAll().size();

        // Get the realTimeEventImage
        restRealTimeEventImageMockMvc.perform(delete("/api/real-time-event-images/{id}", realTimeEventImage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RealTimeEventImage> realTimeEventImageList = realTimeEventImageRepository.findAll();
        assertThat(realTimeEventImageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RealTimeEventImage.class);
    }
}
