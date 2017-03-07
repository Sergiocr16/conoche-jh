package com.firefly.conoche.web.rest;

import com.firefly.conoche.ConocheApp;

import com.firefly.conoche.domain.EventImage;
import com.firefly.conoche.repository.EventImageRepository;
import com.firefly.conoche.service.EventImageService;
import com.firefly.conoche.service.dto.EventImageDTO;
import com.firefly.conoche.service.mapper.EventImageMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EventImageResource REST controller.
 *
 * @see EventImageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConocheApp.class)
public class EventImageResourceIntTest {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    @Autowired
    private EventImageRepository eventImageRepository;

    @Autowired
    private EventImageMapper eventImageMapper;

    @Autowired
    private EventImageService eventImageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventImageMockMvc;

    private EventImage eventImage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventImageResource eventImageResource = new EventImageResource(eventImageService);
        this.restEventImageMockMvc = MockMvcBuilders.standaloneSetup(eventImageResource)
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
    public static EventImage createEntity(EntityManager em) {
        EventImage eventImage = new EventImage()
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
                .imageUrl(DEFAULT_IMAGE_URL);
        return eventImage;
    }

    @Before
    public void initTest() {
        eventImage = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventImage() throws Exception {
        int databaseSizeBeforeCreate = eventImageRepository.findAll().size();

        // Create the EventImage
        EventImageDTO eventImageDTO = eventImageMapper.eventImageToEventImageDTO(eventImage);

        restEventImageMockMvc.perform(post("/api/event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventImageDTO)))
            .andExpect(status().isCreated());

        // Validate the EventImage in the database
        List<EventImage> eventImageList = eventImageRepository.findAll();
        assertThat(eventImageList).hasSize(databaseSizeBeforeCreate + 1);
        EventImage testEventImage = eventImageList.get(eventImageList.size() - 1);
        assertThat(testEventImage.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testEventImage.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testEventImage.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    public void createEventImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventImageRepository.findAll().size();

        // Create the EventImage with an existing ID
        EventImage existingEventImage = new EventImage();
        existingEventImage.setId(1L);
        EventImageDTO existingEventImageDTO = eventImageMapper.eventImageToEventImageDTO(existingEventImage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventImageMockMvc.perform(post("/api/event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEventImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EventImage> eventImageList = eventImageRepository.findAll();
        assertThat(eventImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEventImages() throws Exception {
        // Initialize the database
        eventImageRepository.saveAndFlush(eventImage);

        // Get all the eventImageList
        restEventImageMockMvc.perform(get("/api/event-images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())));
    }

    @Test
    @Transactional
    public void getEventImage() throws Exception {
        // Initialize the database
        eventImageRepository.saveAndFlush(eventImage);

        // Get the eventImage
        restEventImageMockMvc.perform(get("/api/event-images/{id}", eventImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventImage.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventImage() throws Exception {
        // Get the eventImage
        restEventImageMockMvc.perform(get("/api/event-images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventImage() throws Exception {
        // Initialize the database
        eventImageRepository.saveAndFlush(eventImage);
        int databaseSizeBeforeUpdate = eventImageRepository.findAll().size();

        // Update the eventImage
        EventImage updatedEventImage = eventImageRepository.findOne(eventImage.getId());
        updatedEventImage
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
                .imageUrl(UPDATED_IMAGE_URL);
        EventImageDTO eventImageDTO = eventImageMapper.eventImageToEventImageDTO(updatedEventImage);

        restEventImageMockMvc.perform(put("/api/event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventImageDTO)))
            .andExpect(status().isOk());

        // Validate the EventImage in the database
        List<EventImage> eventImageList = eventImageRepository.findAll();
        assertThat(eventImageList).hasSize(databaseSizeBeforeUpdate);
        EventImage testEventImage = eventImageList.get(eventImageList.size() - 1);
        assertThat(testEventImage.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testEventImage.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testEventImage.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingEventImage() throws Exception {
        int databaseSizeBeforeUpdate = eventImageRepository.findAll().size();

        // Create the EventImage
        EventImageDTO eventImageDTO = eventImageMapper.eventImageToEventImageDTO(eventImage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEventImageMockMvc.perform(put("/api/event-images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventImageDTO)))
            .andExpect(status().isCreated());

        // Validate the EventImage in the database
        List<EventImage> eventImageList = eventImageRepository.findAll();
        assertThat(eventImageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEventImage() throws Exception {
        // Initialize the database
        eventImageRepository.saveAndFlush(eventImage);
        int databaseSizeBeforeDelete = eventImageRepository.findAll().size();

        // Get the eventImage
        restEventImageMockMvc.perform(delete("/api/event-images/{id}", eventImage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EventImage> eventImageList = eventImageRepository.findAll();
        assertThat(eventImageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventImage.class);
    }
}
