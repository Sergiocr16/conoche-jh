package com.firefly.conoche.web.rest;

import com.firefly.conoche.ConocheApp;

import com.firefly.conoche.domain.Schedule;
import com.firefly.conoche.repository.ScheduleRepository;
import com.firefly.conoche.service.ScheduleService;
import com.firefly.conoche.service.dto.ScheduleDTO;
import com.firefly.conoche.service.mapper.ScheduleMapper;
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

import com.firefly.conoche.domain.enumeration.Day;
import com.firefly.conoche.domain.enumeration.Day;
/**
 * Test class for the ScheduleResource REST controller.
 *
 * @see ScheduleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConocheApp.class)
public class ScheduleResourceIntTest {

    private static final Day DEFAULT_INITIAL_DAY = Day.MONDAY;
    private static final Day UPDATED_INITIAL_DAY = Day.TUESDAY;

    private static final Day DEFAULT_FINAL_DAY = Day.MONDAY;
    private static final Day UPDATED_FINAL_DAY = Day.TUESDAY;

    private static final Integer DEFAULT_INITIAL_TIME = 0;
    private static final Integer UPDATED_INITIAL_TIME = 1;

    private static final Integer DEFAULT_FINAL_TIME = 0;
    private static final Integer UPDATED_FINAL_TIME = 1;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restScheduleMockMvc;

    private Schedule schedule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScheduleResource scheduleResource = new ScheduleResource(scheduleService);
        this.restScheduleMockMvc = MockMvcBuilders.standaloneSetup(scheduleResource)
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
    public static Schedule createEntity(EntityManager em) {
        Schedule schedule = new Schedule()
                .initialDay(DEFAULT_INITIAL_DAY)
                .finalDay(DEFAULT_FINAL_DAY)
                .initialTime(DEFAULT_INITIAL_TIME)
                .finalTime(DEFAULT_FINAL_TIME);
        return schedule;
    }

    @Before
    public void initTest() {
        schedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchedule() throws Exception {
        int databaseSizeBeforeCreate = scheduleRepository.findAll().size();

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);

        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeCreate + 1);
        Schedule testSchedule = scheduleList.get(scheduleList.size() - 1);
        assertThat(testSchedule.getInitialDay()).isEqualTo(DEFAULT_INITIAL_DAY);
        assertThat(testSchedule.getFinalDay()).isEqualTo(DEFAULT_FINAL_DAY);
        assertThat(testSchedule.getInitialTime()).isEqualTo(DEFAULT_INITIAL_TIME);
        assertThat(testSchedule.getFinalTime()).isEqualTo(DEFAULT_FINAL_TIME);
    }

    @Test
    @Transactional
    public void createScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scheduleRepository.findAll().size();

        // Create the Schedule with an existing ID
        Schedule existingSchedule = new Schedule();
        existingSchedule.setId(1L);
        ScheduleDTO existingScheduleDTO = scheduleMapper.scheduleToScheduleDTO(existingSchedule);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingScheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInitialDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduleRepository.findAll().size();
        // set the field null
        schedule.setInitialDay(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);

        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinalDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduleRepository.findAll().size();
        // set the field null
        schedule.setFinalDay(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);

        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInitialTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduleRepository.findAll().size();
        // set the field null
        schedule.setInitialTime(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);

        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinalTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduleRepository.findAll().size();
        // set the field null
        schedule.setFinalTime(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);

        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSchedules() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList
        restScheduleMockMvc.perform(get("/api/schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].initialDay").value(hasItem(DEFAULT_INITIAL_DAY.toString())))
            .andExpect(jsonPath("$.[*].finalDay").value(hasItem(DEFAULT_FINAL_DAY.toString())))
            .andExpect(jsonPath("$.[*].initialTime").value(hasItem(DEFAULT_INITIAL_TIME)))
            .andExpect(jsonPath("$.[*].finalTime").value(hasItem(DEFAULT_FINAL_TIME)));
    }

    @Test
    @Transactional
    public void getSchedule() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get the schedule
        restScheduleMockMvc.perform(get("/api/schedules/{id}", schedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schedule.getId().intValue()))
            .andExpect(jsonPath("$.initialDay").value(DEFAULT_INITIAL_DAY.toString()))
            .andExpect(jsonPath("$.finalDay").value(DEFAULT_FINAL_DAY.toString()))
            .andExpect(jsonPath("$.initialTime").value(DEFAULT_INITIAL_TIME))
            .andExpect(jsonPath("$.finalTime").value(DEFAULT_FINAL_TIME));
    }

    @Test
    @Transactional
    public void getNonExistingSchedule() throws Exception {
        // Get the schedule
        restScheduleMockMvc.perform(get("/api/schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchedule() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();

        // Update the schedule
        Schedule updatedSchedule = scheduleRepository.findOne(schedule.getId());
        updatedSchedule
                .initialDay(UPDATED_INITIAL_DAY)
                .finalDay(UPDATED_FINAL_DAY)
                .initialTime(UPDATED_INITIAL_TIME)
                .finalTime(UPDATED_FINAL_TIME);
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(updatedSchedule);

        restScheduleMockMvc.perform(put("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isOk());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
        Schedule testSchedule = scheduleList.get(scheduleList.size() - 1);
        assertThat(testSchedule.getInitialDay()).isEqualTo(UPDATED_INITIAL_DAY);
        assertThat(testSchedule.getFinalDay()).isEqualTo(UPDATED_FINAL_DAY);
        assertThat(testSchedule.getInitialTime()).isEqualTo(UPDATED_INITIAL_TIME);
        assertThat(testSchedule.getFinalTime()).isEqualTo(UPDATED_FINAL_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingSchedule() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restScheduleMockMvc.perform(put("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSchedule() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);
        int databaseSizeBeforeDelete = scheduleRepository.findAll().size();

        // Get the schedule
        restScheduleMockMvc.perform(delete("/api/schedules/{id}", schedule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Schedule.class);
    }
}
