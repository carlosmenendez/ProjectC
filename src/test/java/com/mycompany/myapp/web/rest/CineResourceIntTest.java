package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BCiNeApp;

import com.mycompany.myapp.domain.Cine;
import com.mycompany.myapp.repository.CineRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

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

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CineResource REST controller.
 *
 * @see CineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BCiNeApp.class)
public class CineResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final Integer DEFAULT_TELEFONO = 1;
    private static final Integer UPDATED_TELEFONO = 2;

    private static final Integer DEFAULT_NUM_DE_SALAS = 1;
    private static final Integer UPDATED_NUM_DE_SALAS = 2;

    @Autowired
    private CineRepository cineRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCineMockMvc;

    private Cine cine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CineResource cineResource = new CineResource(cineRepository);
        this.restCineMockMvc = MockMvcBuilders.standaloneSetup(cineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cine createEntity(EntityManager em) {
        Cine cine = new Cine()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .ciudad(DEFAULT_CIUDAD)
            .telefono(DEFAULT_TELEFONO)
            .numDeSalas(DEFAULT_NUM_DE_SALAS);
        return cine;
    }

    @Before
    public void initTest() {
        cine = createEntity(em);
    }

    @Test
    @Transactional
    public void createCine() throws Exception {
        int databaseSizeBeforeCreate = cineRepository.findAll().size();

        // Create the Cine
        restCineMockMvc.perform(post("/api/cines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cine)))
            .andExpect(status().isCreated());

        // Validate the Cine in the database
        List<Cine> cineList = cineRepository.findAll();
        assertThat(cineList).hasSize(databaseSizeBeforeCreate + 1);
        Cine testCine = cineList.get(cineList.size() - 1);
        assertThat(testCine.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCine.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testCine.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testCine.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testCine.getNumDeSalas()).isEqualTo(DEFAULT_NUM_DE_SALAS);
    }

    @Test
    @Transactional
    public void createCineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cineRepository.findAll().size();

        // Create the Cine with an existing ID
        cine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCineMockMvc.perform(post("/api/cines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cine)))
            .andExpect(status().isBadRequest());

        // Validate the Cine in the database
        List<Cine> cineList = cineRepository.findAll();
        assertThat(cineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCines() throws Exception {
        // Initialize the database
        cineRepository.saveAndFlush(cine);

        // Get all the cineList
        restCineMockMvc.perform(get("/api/cines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cine.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].numDeSalas").value(hasItem(DEFAULT_NUM_DE_SALAS)));
    }

    @Test
    @Transactional
    public void getCine() throws Exception {
        // Initialize the database
        cineRepository.saveAndFlush(cine);

        // Get the cine
        restCineMockMvc.perform(get("/api/cines/{id}", cine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cine.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.numDeSalas").value(DEFAULT_NUM_DE_SALAS));
    }

    @Test
    @Transactional
    public void getNonExistingCine() throws Exception {
        // Get the cine
        restCineMockMvc.perform(get("/api/cines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCine() throws Exception {
        // Initialize the database
        cineRepository.saveAndFlush(cine);
        int databaseSizeBeforeUpdate = cineRepository.findAll().size();

        // Update the cine
        Cine updatedCine = cineRepository.findOne(cine.getId());
        // Disconnect from session so that the updates on updatedCine are not directly saved in db
        em.detach(updatedCine);
        updatedCine
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .ciudad(UPDATED_CIUDAD)
            .telefono(UPDATED_TELEFONO)
            .numDeSalas(UPDATED_NUM_DE_SALAS);

        restCineMockMvc.perform(put("/api/cines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCine)))
            .andExpect(status().isOk());

        // Validate the Cine in the database
        List<Cine> cineList = cineRepository.findAll();
        assertThat(cineList).hasSize(databaseSizeBeforeUpdate);
        Cine testCine = cineList.get(cineList.size() - 1);
        assertThat(testCine.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCine.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testCine.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testCine.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testCine.getNumDeSalas()).isEqualTo(UPDATED_NUM_DE_SALAS);
    }

    @Test
    @Transactional
    public void updateNonExistingCine() throws Exception {
        int databaseSizeBeforeUpdate = cineRepository.findAll().size();

        // Create the Cine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCineMockMvc.perform(put("/api/cines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cine)))
            .andExpect(status().isCreated());

        // Validate the Cine in the database
        List<Cine> cineList = cineRepository.findAll();
        assertThat(cineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCine() throws Exception {
        // Initialize the database
        cineRepository.saveAndFlush(cine);
        int databaseSizeBeforeDelete = cineRepository.findAll().size();

        // Get the cine
        restCineMockMvc.perform(delete("/api/cines/{id}", cine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cine> cineList = cineRepository.findAll();
        assertThat(cineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cine.class);
        Cine cine1 = new Cine();
        cine1.setId(1L);
        Cine cine2 = new Cine();
        cine2.setId(cine1.getId());
        assertThat(cine1).isEqualTo(cine2);
        cine2.setId(2L);
        assertThat(cine1).isNotEqualTo(cine2);
        cine1.setId(null);
        assertThat(cine1).isNotEqualTo(cine2);
    }
}
