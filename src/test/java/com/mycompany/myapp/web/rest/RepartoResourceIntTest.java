package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BCiNeApp;

import com.mycompany.myapp.domain.Reparto;
import com.mycompany.myapp.repository.RepartoRepository;
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
 * Test class for the RepartoResource REST controller.
 *
 * @see RepartoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BCiNeApp.class)
public class RepartoResourceIntTest {

    private static final Integer DEFAULT_NOMBRE_ARTISTA = 1;
    private static final Integer UPDATED_NOMBRE_ARTISTA = 2;

    private static final Integer DEFAULT_ROL = 1;
    private static final Integer UPDATED_ROL = 2;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PELICULA = 1;
    private static final Integer UPDATED_PELICULA = 2;

    @Autowired
    private RepartoRepository repartoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRepartoMockMvc;

    private Reparto reparto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RepartoResource repartoResource = new RepartoResource(repartoRepository);
        this.restRepartoMockMvc = MockMvcBuilders.standaloneSetup(repartoResource)
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
    public static Reparto createEntity(EntityManager em) {
        Reparto reparto = new Reparto()
            .nombreArtista(DEFAULT_NOMBRE_ARTISTA)
            .rol(DEFAULT_ROL)
            .descripcion(DEFAULT_DESCRIPCION)
            .pelicula(DEFAULT_PELICULA);
        return reparto;
    }

    @Before
    public void initTest() {
        reparto = createEntity(em);
    }

    @Test
    @Transactional
    public void createReparto() throws Exception {
        int databaseSizeBeforeCreate = repartoRepository.findAll().size();

        // Create the Reparto
        restRepartoMockMvc.perform(post("/api/repartos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reparto)))
            .andExpect(status().isCreated());

        // Validate the Reparto in the database
        List<Reparto> repartoList = repartoRepository.findAll();
        assertThat(repartoList).hasSize(databaseSizeBeforeCreate + 1);
        Reparto testReparto = repartoList.get(repartoList.size() - 1);
        assertThat(testReparto.getNombreArtista()).isEqualTo(DEFAULT_NOMBRE_ARTISTA);
        assertThat(testReparto.getRol()).isEqualTo(DEFAULT_ROL);
        assertThat(testReparto.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testReparto.getPelicula()).isEqualTo(DEFAULT_PELICULA);
    }

    @Test
    @Transactional
    public void createRepartoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = repartoRepository.findAll().size();

        // Create the Reparto with an existing ID
        reparto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepartoMockMvc.perform(post("/api/repartos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reparto)))
            .andExpect(status().isBadRequest());

        // Validate the Reparto in the database
        List<Reparto> repartoList = repartoRepository.findAll();
        assertThat(repartoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRepartos() throws Exception {
        // Initialize the database
        repartoRepository.saveAndFlush(reparto);

        // Get all the repartoList
        restRepartoMockMvc.perform(get("/api/repartos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reparto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreArtista").value(hasItem(DEFAULT_NOMBRE_ARTISTA)))
            .andExpect(jsonPath("$.[*].rol").value(hasItem(DEFAULT_ROL)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].pelicula").value(hasItem(DEFAULT_PELICULA)));
    }

    @Test
    @Transactional
    public void getReparto() throws Exception {
        // Initialize the database
        repartoRepository.saveAndFlush(reparto);

        // Get the reparto
        restRepartoMockMvc.perform(get("/api/repartos/{id}", reparto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reparto.getId().intValue()))
            .andExpect(jsonPath("$.nombreArtista").value(DEFAULT_NOMBRE_ARTISTA))
            .andExpect(jsonPath("$.rol").value(DEFAULT_ROL))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.pelicula").value(DEFAULT_PELICULA));
    }

    @Test
    @Transactional
    public void getNonExistingReparto() throws Exception {
        // Get the reparto
        restRepartoMockMvc.perform(get("/api/repartos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReparto() throws Exception {
        // Initialize the database
        repartoRepository.saveAndFlush(reparto);
        int databaseSizeBeforeUpdate = repartoRepository.findAll().size();

        // Update the reparto
        Reparto updatedReparto = repartoRepository.findOne(reparto.getId());
        // Disconnect from session so that the updates on updatedReparto are not directly saved in db
        em.detach(updatedReparto);
        updatedReparto
            .nombreArtista(UPDATED_NOMBRE_ARTISTA)
            .rol(UPDATED_ROL)
            .descripcion(UPDATED_DESCRIPCION)
            .pelicula(UPDATED_PELICULA);

        restRepartoMockMvc.perform(put("/api/repartos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReparto)))
            .andExpect(status().isOk());

        // Validate the Reparto in the database
        List<Reparto> repartoList = repartoRepository.findAll();
        assertThat(repartoList).hasSize(databaseSizeBeforeUpdate);
        Reparto testReparto = repartoList.get(repartoList.size() - 1);
        assertThat(testReparto.getNombreArtista()).isEqualTo(UPDATED_NOMBRE_ARTISTA);
        assertThat(testReparto.getRol()).isEqualTo(UPDATED_ROL);
        assertThat(testReparto.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testReparto.getPelicula()).isEqualTo(UPDATED_PELICULA);
    }

    @Test
    @Transactional
    public void updateNonExistingReparto() throws Exception {
        int databaseSizeBeforeUpdate = repartoRepository.findAll().size();

        // Create the Reparto

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRepartoMockMvc.perform(put("/api/repartos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reparto)))
            .andExpect(status().isCreated());

        // Validate the Reparto in the database
        List<Reparto> repartoList = repartoRepository.findAll();
        assertThat(repartoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReparto() throws Exception {
        // Initialize the database
        repartoRepository.saveAndFlush(reparto);
        int databaseSizeBeforeDelete = repartoRepository.findAll().size();

        // Get the reparto
        restRepartoMockMvc.perform(delete("/api/repartos/{id}", reparto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reparto> repartoList = repartoRepository.findAll();
        assertThat(repartoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reparto.class);
        Reparto reparto1 = new Reparto();
        reparto1.setId(1L);
        Reparto reparto2 = new Reparto();
        reparto2.setId(reparto1.getId());
        assertThat(reparto1).isEqualTo(reparto2);
        reparto2.setId(2L);
        assertThat(reparto1).isNotEqualTo(reparto2);
        reparto1.setId(null);
        assertThat(reparto1).isNotEqualTo(reparto2);
    }
}
