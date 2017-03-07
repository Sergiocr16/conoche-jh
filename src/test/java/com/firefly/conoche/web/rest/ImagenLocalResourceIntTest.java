package com.firefly.conoche.web.rest;

import com.firefly.conoche.ConocheApp;

import com.firefly.conoche.domain.ImagenLocal;
import com.firefly.conoche.repository.ImagenLocalRepository;
import com.firefly.conoche.service.ImagenLocalService;
import com.firefly.conoche.service.dto.ImagenLocalDTO;
import com.firefly.conoche.service.mapper.ImagenLocalMapper;
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
 * Test class for the ImagenLocalResource REST controller.
 *
 * @see ImagenLocalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConocheApp.class)
public class ImagenLocalResourceIntTest {

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ImagenLocalRepository imagenLocalRepository;

    @Autowired
    private ImagenLocalMapper imagenLocalMapper;

    @Autowired
    private ImagenLocalService imagenLocalService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restImagenLocalMockMvc;

    private ImagenLocal imagenLocal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImagenLocalResource imagenLocalResource = new ImagenLocalResource(imagenLocalService);
        this.restImagenLocalMockMvc = MockMvcBuilders.standaloneSetup(imagenLocalResource)
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
    public static ImagenLocal createEntity(EntityManager em) {
        ImagenLocal imagenLocal = new ImagenLocal()
                .imageUrl(DEFAULT_IMAGE_URL)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return imagenLocal;
    }

    @Before
    public void initTest() {
        imagenLocal = createEntity(em);
    }

    @Test
    @Transactional
    public void createImagenLocal() throws Exception {
        int databaseSizeBeforeCreate = imagenLocalRepository.findAll().size();

        // Create the ImagenLocal
        ImagenLocalDTO imagenLocalDTO = imagenLocalMapper.imagenLocalToImagenLocalDTO(imagenLocal);

        restImagenLocalMockMvc.perform(post("/api/imagen-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imagenLocalDTO)))
            .andExpect(status().isCreated());

        // Validate the ImagenLocal in the database
        List<ImagenLocal> imagenLocalList = imagenLocalRepository.findAll();
        assertThat(imagenLocalList).hasSize(databaseSizeBeforeCreate + 1);
        ImagenLocal testImagenLocal = imagenLocalList.get(imagenLocalList.size() - 1);
        assertThat(testImagenLocal.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testImagenLocal.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testImagenLocal.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createImagenLocalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = imagenLocalRepository.findAll().size();

        // Create the ImagenLocal with an existing ID
        ImagenLocal existingImagenLocal = new ImagenLocal();
        existingImagenLocal.setId(1L);
        ImagenLocalDTO existingImagenLocalDTO = imagenLocalMapper.imagenLocalToImagenLocalDTO(existingImagenLocal);

        // An entity with an existing ID cannot be created, so this API call must fail
        restImagenLocalMockMvc.perform(post("/api/imagen-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingImagenLocalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ImagenLocal> imagenLocalList = imagenLocalRepository.findAll();
        assertThat(imagenLocalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllImagenLocals() throws Exception {
        // Initialize the database
        imagenLocalRepository.saveAndFlush(imagenLocal);

        // Get all the imagenLocalList
        restImagenLocalMockMvc.perform(get("/api/imagen-locals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imagenLocal.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void getImagenLocal() throws Exception {
        // Initialize the database
        imagenLocalRepository.saveAndFlush(imagenLocal);

        // Get the imagenLocal
        restImagenLocalMockMvc.perform(get("/api/imagen-locals/{id}", imagenLocal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(imagenLocal.getId().intValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingImagenLocal() throws Exception {
        // Get the imagenLocal
        restImagenLocalMockMvc.perform(get("/api/imagen-locals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImagenLocal() throws Exception {
        // Initialize the database
        imagenLocalRepository.saveAndFlush(imagenLocal);
        int databaseSizeBeforeUpdate = imagenLocalRepository.findAll().size();

        // Update the imagenLocal
        ImagenLocal updatedImagenLocal = imagenLocalRepository.findOne(imagenLocal.getId());
        updatedImagenLocal
                .imageUrl(UPDATED_IMAGE_URL)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ImagenLocalDTO imagenLocalDTO = imagenLocalMapper.imagenLocalToImagenLocalDTO(updatedImagenLocal);

        restImagenLocalMockMvc.perform(put("/api/imagen-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imagenLocalDTO)))
            .andExpect(status().isOk());

        // Validate the ImagenLocal in the database
        List<ImagenLocal> imagenLocalList = imagenLocalRepository.findAll();
        assertThat(imagenLocalList).hasSize(databaseSizeBeforeUpdate);
        ImagenLocal testImagenLocal = imagenLocalList.get(imagenLocalList.size() - 1);
        assertThat(testImagenLocal.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testImagenLocal.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImagenLocal.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingImagenLocal() throws Exception {
        int databaseSizeBeforeUpdate = imagenLocalRepository.findAll().size();

        // Create the ImagenLocal
        ImagenLocalDTO imagenLocalDTO = imagenLocalMapper.imagenLocalToImagenLocalDTO(imagenLocal);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restImagenLocalMockMvc.perform(put("/api/imagen-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(imagenLocalDTO)))
            .andExpect(status().isCreated());

        // Validate the ImagenLocal in the database
        List<ImagenLocal> imagenLocalList = imagenLocalRepository.findAll();
        assertThat(imagenLocalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteImagenLocal() throws Exception {
        // Initialize the database
        imagenLocalRepository.saveAndFlush(imagenLocal);
        int databaseSizeBeforeDelete = imagenLocalRepository.findAll().size();

        // Get the imagenLocal
        restImagenLocalMockMvc.perform(delete("/api/imagen-locals/{id}", imagenLocal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ImagenLocal> imagenLocalList = imagenLocalRepository.findAll();
        assertThat(imagenLocalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImagenLocal.class);
    }
}
