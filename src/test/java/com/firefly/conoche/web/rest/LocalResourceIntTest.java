package com.firefly.conoche.web.rest;

import com.firefly.conoche.ConocheApp;

import com.firefly.conoche.domain.Local;
import com.firefly.conoche.domain.User;
import com.firefly.conoche.repository.LocalRepository;
import com.firefly.conoche.service.LocalService;
import com.firefly.conoche.service.dto.LocalDTO;
import com.firefly.conoche.service.mapper.LocalMapper;
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

import com.firefly.conoche.domain.enumeration.Provincia;
/**
 * Test class for the LocalResource REST controller.
 *
 * @see LocalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConocheApp.class)
public class LocalResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_LONGITUD = 1D;
    private static final Double UPDATED_LONGITUD = 2D;

    private static final byte[] DEFAULT_BANNER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BANNER = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_BANNER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BANNER_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_BANNER_URL = "AAAAAAAAAA";
    private static final String UPDATED_BANNER_URL = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUD = 1D;
    private static final Double UPDATED_LATITUD = 2D;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Provincia DEFAULT_PROVINCIA = Provincia.SAN_JOSE;
    private static final Provincia UPDATED_PROVINCIA = Provincia.ALAJUELA;

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private LocalMapper localMapper;

    @Autowired
    private LocalService localService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLocalMockMvc;

    private Local local;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocalResource localResource = new LocalResource(localService);
        this.restLocalMockMvc = MockMvcBuilders.standaloneSetup(localResource)
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
    public static Local createEntity(EntityManager em) {
        Local local = new Local()
                .name(DEFAULT_NAME)
                .longitud(DEFAULT_LONGITUD)
                .banner(DEFAULT_BANNER)
                .bannerContentType(DEFAULT_BANNER_CONTENT_TYPE)
                .bannerUrl(DEFAULT_BANNER_URL)
                .latitud(DEFAULT_LATITUD)
                .descripcion(DEFAULT_DESCRIPCION)
                .provincia(DEFAULT_PROVINCIA)
                .rating(DEFAULT_RATING);
        // Add required entity
        User owner = UserResourceIntTest.createEntity(em);
        em.persist(owner);
        em.flush();
        local.setOwner(owner);
        return local;
    }

    @Before
    public void initTest() {
        local = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocal() throws Exception {
        int databaseSizeBeforeCreate = localRepository.findAll().size();

        // Create the Local
        LocalDTO localDTO = localMapper.localToLocalDTO(local);

        restLocalMockMvc.perform(post("/api/locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isCreated());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeCreate + 1);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLocal.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testLocal.getBanner()).isEqualTo(DEFAULT_BANNER);
        assertThat(testLocal.getBannerContentType()).isEqualTo(DEFAULT_BANNER_CONTENT_TYPE);
        assertThat(testLocal.getBannerUrl()).isEqualTo(DEFAULT_BANNER_URL);
        assertThat(testLocal.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testLocal.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testLocal.getProvincia()).isEqualTo(DEFAULT_PROVINCIA);
        assertThat(testLocal.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void createLocalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = localRepository.findAll().size();

        // Create the Local with an existing ID
        Local existingLocal = new Local();
        existingLocal.setId(1L);
        LocalDTO existingLocalDTO = localMapper.localToLocalDTO(existingLocal);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalMockMvc.perform(post("/api/locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingLocalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = localRepository.findAll().size();
        // set the field null
        local.setName(null);

        // Create the Local, which fails.
        LocalDTO localDTO = localMapper.localToLocalDTO(local);

        restLocalMockMvc.perform(post("/api/locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isBadRequest());

        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocals() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList
        restLocalMockMvc.perform(get("/api/locals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(local.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.doubleValue())))
            .andExpect(jsonPath("$.[*].bannerContentType").value(hasItem(DEFAULT_BANNER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].banner").value(hasItem(Base64Utils.encodeToString(DEFAULT_BANNER))))
            .andExpect(jsonPath("$.[*].bannerUrl").value(hasItem(DEFAULT_BANNER_URL.toString())))
            .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.doubleValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].provincia").value(hasItem(DEFAULT_PROVINCIA.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())));
    }

    @Test
    @Transactional
    public void getLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get the local
        restLocalMockMvc.perform(get("/api/locals/{id}", local.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(local.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD.doubleValue()))
            .andExpect(jsonPath("$.bannerContentType").value(DEFAULT_BANNER_CONTENT_TYPE))
            .andExpect(jsonPath("$.banner").value(Base64Utils.encodeToString(DEFAULT_BANNER)))
            .andExpect(jsonPath("$.bannerUrl").value(DEFAULT_BANNER_URL.toString()))
            .andExpect(jsonPath("$.latitud").value(DEFAULT_LATITUD.doubleValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.provincia").value(DEFAULT_PROVINCIA.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLocal() throws Exception {
        // Get the local
        restLocalMockMvc.perform(get("/api/locals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);
        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Update the local
        Local updatedLocal = localRepository.findOne(local.getId());
        updatedLocal
                .name(UPDATED_NAME)
                .longitud(UPDATED_LONGITUD)
                .banner(UPDATED_BANNER)
                .bannerContentType(UPDATED_BANNER_CONTENT_TYPE)
                .bannerUrl(UPDATED_BANNER_URL)
                .latitud(UPDATED_LATITUD)
                .descripcion(UPDATED_DESCRIPCION)
                .provincia(UPDATED_PROVINCIA)
                .rating(UPDATED_RATING);
        LocalDTO localDTO = localMapper.localToLocalDTO(updatedLocal);

        restLocalMockMvc.perform(put("/api/locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isOk());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLocal.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testLocal.getBanner()).isEqualTo(UPDATED_BANNER);
        assertThat(testLocal.getBannerContentType()).isEqualTo(UPDATED_BANNER_CONTENT_TYPE);
        assertThat(testLocal.getBannerUrl()).isEqualTo(UPDATED_BANNER_URL);
        assertThat(testLocal.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testLocal.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testLocal.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
        assertThat(testLocal.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    public void updateNonExistingLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Create the Local
        LocalDTO localDTO = localMapper.localToLocalDTO(local);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLocalMockMvc.perform(put("/api/locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localDTO)))
            .andExpect(status().isCreated());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);
        int databaseSizeBeforeDelete = localRepository.findAll().size();

        // Get the local
        restLocalMockMvc.perform(delete("/api/locals/{id}", local.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Local.class);
    }
}
