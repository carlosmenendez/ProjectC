package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BCiNeApp;

import com.mycompany.myapp.domain.Salas;
import com.mycompany.myapp.repository.SalasRepository;
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
 * Test class for the SalasResource REST controller.
 *
 * @see SalasResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BCiNeApp.class)
public class SalasResourceIntTest {

    private static final Integer DEFAULT_DENOMINACION = 1;
    private static final Integer UPDATED_DENOMINACION = 2;

    private static final Integer DEFAULT_AFORO = 1;
    private static final Integer UPDATED_AFORO = 2;

    private static final Integer DEFAULT_CINE = 1;
    private static final Integer UPDATED_CINE = 2;

    @Autowired
    private SalasRepository salasRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalasMockMvc;

    private Salas salas;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalasResource salasResource = new SalasResource(salasRepository);
        this.restSalasMockMvc = MockMvcBuilders.standaloneSetup(salasResource)
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
    public static Salas createEntity(EntityManager em) {
        Salas salas = new Salas()
            .denominacion(DEFAULT_DENOMINACION)
            .aforo(DEFAULT_AFORO)
            .cine(DEFAULT_CINE);
        return salas;
    }

    @Before
    public void initTest() {
        salas = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalas() throws Exception {
        int databaseSizeBeforeCreate = salasRepository.findAll().size();

        // Create the Salas
        restSalasMockMvc.perform(post("/api/salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salas)))
            .andExpect(status().isCreated());

        // Validate the Salas in the database
        List<Salas> salasList = salasRepository.findAll();
        assertThat(salasList).hasSize(databaseSizeBeforeCreate + 1);
        Salas testSalas = salasList.get(salasList.size() - 1);
        assertThat(testSalas.getDenominacion()).isEqualTo(DEFAULT_DENOMINACION);
        assertThat(testSalas.getAforo()).isEqualTo(DEFAULT_AFORO);
        assertThat(testSalas.getCine()).isEqualTo(DEFAULT_CINE);
    }

    @Test
    @Transactional
    public void createSalasWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salasRepository.findAll().size();

        // Create the Salas with an existing ID
        salas.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalasMockMvc.perform(post("/api/salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salas)))
            .andExpect(status().isBadRequest());

        // Validate the Salas in the database
        List<Salas> salasList = salasRepository.findAll();
        assertThat(salasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSalas() throws Exception {
        // Initialize the database
        salasRepository.saveAndFlush(salas);

        // Get all the salasList
        restSalasMockMvc.perform(get("/api/salas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salas.getId().intValue())))
            .andExpect(jsonPath("$.[*].denominacion").value(hasItem(DEFAULT_DENOMINACION)))
            .andExpect(jsonPath("$.[*].aforo").value(hasItem(DEFAULT_AFORO)))
            .andExpect(jsonPath("$.[*].cine").value(hasItem(DEFAULT_CINE)));
    }

    @Test
    @Transactional
    public void getSalas() throws Exception {
        // Initialize the database
        salasRepository.saveAndFlush(salas);

        // Get the salas
        restSalasMockMvc.perform(get("/api/salas/{id}", salas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salas.getId().intValue()))
            .andExpect(jsonPath("$.denominacion").value(DEFAULT_DENOMINACION))
            .andExpect(jsonPath("$.aforo").value(DEFAULT_AFORO))
            .andExpect(jsonPath("$.cine").value(DEFAULT_CINE));
    }

    @Test
    @Transactional
    public void getNonExistingSalas() throws Exception {
        // Get the salas
        restSalasMockMvc.perform(get("/api/salas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalas() throws Exception {
        // Initialize the database
        salasRepository.saveAndFlush(salas);
        int databaseSizeBeforeUpdate = salasRepository.findAll().size();

        // Update the salas
        Salas updatedSalas = salasRepository.findOne(salas.getId());
        // Disconnect from session so that the updates on updatedSalas are not directly saved in db
        em.detach(updatedSalas);
        updatedSalas
            .denominacion(UPDATED_DENOMINACION)
            .aforo(UPDATED_AFORO)
            .cine(UPDATED_CINE);

        restSalasMockMvc.perform(put("/api/salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalas)))
            .andExpect(status().isOk());

        // Validate the Salas in the database
        List<Salas> salasList = salasRepository.findAll();
        assertThat(salasList).hasSize(databaseSizeBeforeUpdate);
        Salas testSalas = salasList.get(salasList.size() - 1);
        assertThat(testSalas.getDenominacion()).isEqualTo(UPDATED_DENOMINACION);
        assertThat(testSalas.getAforo()).isEqualTo(UPDATED_AFORO);
        assertThat(testSalas.getCine()).isEqualTo(UPDATED_CINE);
    }

    @Test
    @Transactional
    public void updateNonExistingSalas() throws Exception {
        int databaseSizeBeforeUpdate = salasRepository.findAll().size();

        // Create the Salas

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalasMockMvc.perform(put("/api/salas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salas)))
            .andExpect(status().isCreated());

        // Validate the Salas in the database
        List<Salas> salasList = salasRepository.findAll();
        assertThat(salasList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSalas() throws Exception {
        // Initialize the database
        salasRepository.saveAndFlush(salas);
        int databaseSizeBeforeDelete = salasRepository.findAll().size();

        // Get the salas
        restSalasMockMvc.perform(delete("/api/salas/{id}", salas.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Salas> salasList = salasRepository.findAll();
        assertThat(salasList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Salas.class);
        Salas salas1 = new Salas();
        salas1.setId(1L);
        Salas salas2 = new Salas();
        salas2.setId(salas1.getId());
        assertThat(salas1).isEqualTo(salas2);
        salas2.setId(2L);
        assertThat(salas1).isNotEqualTo(salas2);
        salas1.setId(null);
        assertThat(salas1).isNotEqualTo(salas2);
    }
}
