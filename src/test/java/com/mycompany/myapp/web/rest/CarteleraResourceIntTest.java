package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BCiNeApp;

import com.mycompany.myapp.domain.Cartelera;
import com.mycompany.myapp.repository.CarteleraRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CarteleraResource REST controller.
 *
 * @see CarteleraResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BCiNeApp.class)
public class CarteleraResourceIntTest {

    private static final ZonedDateTime DEFAULT_DIA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DIA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_CINE = 1;
    private static final Integer UPDATED_CINE = 2;

    private static final Integer DEFAULT_PELICULA = 1;
    private static final Integer UPDATED_PELICULA = 2;

    private static final Integer DEFAULT_SALA = 1;
    private static final Integer UPDATED_SALA = 2;

    @Autowired
    private CarteleraRepository carteleraRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCarteleraMockMvc;

    private Cartelera cartelera;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarteleraResource carteleraResource = new CarteleraResource(carteleraRepository);
        this.restCarteleraMockMvc = MockMvcBuilders.standaloneSetup(carteleraResource)
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
    public static Cartelera createEntity(EntityManager em) {
        Cartelera cartelera = new Cartelera()
            .dia(DEFAULT_DIA)
            .cine(DEFAULT_CINE)
            .pelicula(DEFAULT_PELICULA)
            .sala(DEFAULT_SALA);
        return cartelera;
    }

    @Before
    public void initTest() {
        cartelera = createEntity(em);
    }

    @Test
    @Transactional
    public void createCartelera() throws Exception {
        int databaseSizeBeforeCreate = carteleraRepository.findAll().size();

        // Create the Cartelera
        restCarteleraMockMvc.perform(post("/api/carteleras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartelera)))
            .andExpect(status().isCreated());

        // Validate the Cartelera in the database
        List<Cartelera> carteleraList = carteleraRepository.findAll();
        assertThat(carteleraList).hasSize(databaseSizeBeforeCreate + 1);
        Cartelera testCartelera = carteleraList.get(carteleraList.size() - 1);
        assertThat(testCartelera.getDia()).isEqualTo(DEFAULT_DIA);
        assertThat(testCartelera.getCine()).isEqualTo(DEFAULT_CINE);
        assertThat(testCartelera.getPelicula()).isEqualTo(DEFAULT_PELICULA);
        assertThat(testCartelera.getSala()).isEqualTo(DEFAULT_SALA);
    }

    @Test
    @Transactional
    public void createCarteleraWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carteleraRepository.findAll().size();

        // Create the Cartelera with an existing ID
        cartelera.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarteleraMockMvc.perform(post("/api/carteleras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartelera)))
            .andExpect(status().isBadRequest());

        // Validate the Cartelera in the database
        List<Cartelera> carteleraList = carteleraRepository.findAll();
        assertThat(carteleraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCarteleras() throws Exception {
        // Initialize the database
        carteleraRepository.saveAndFlush(cartelera);

        // Get all the carteleraList
        restCarteleraMockMvc.perform(get("/api/carteleras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartelera.getId().intValue())))
            .andExpect(jsonPath("$.[*].dia").value(hasItem(sameInstant(DEFAULT_DIA))))
            .andExpect(jsonPath("$.[*].cine").value(hasItem(DEFAULT_CINE)))
            .andExpect(jsonPath("$.[*].pelicula").value(hasItem(DEFAULT_PELICULA)))
            .andExpect(jsonPath("$.[*].sala").value(hasItem(DEFAULT_SALA)));
    }

    @Test
    @Transactional
    public void getCartelera() throws Exception {
        // Initialize the database
        carteleraRepository.saveAndFlush(cartelera);

        // Get the cartelera
        restCarteleraMockMvc.perform(get("/api/carteleras/{id}", cartelera.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cartelera.getId().intValue()))
            .andExpect(jsonPath("$.dia").value(sameInstant(DEFAULT_DIA)))
            .andExpect(jsonPath("$.cine").value(DEFAULT_CINE))
            .andExpect(jsonPath("$.pelicula").value(DEFAULT_PELICULA))
            .andExpect(jsonPath("$.sala").value(DEFAULT_SALA));
    }

    @Test
    @Transactional
    public void getNonExistingCartelera() throws Exception {
        // Get the cartelera
        restCarteleraMockMvc.perform(get("/api/carteleras/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCartelera() throws Exception {
        // Initialize the database
        carteleraRepository.saveAndFlush(cartelera);
        int databaseSizeBeforeUpdate = carteleraRepository.findAll().size();

        // Update the cartelera
        Cartelera updatedCartelera = carteleraRepository.findOne(cartelera.getId());
        // Disconnect from session so that the updates on updatedCartelera are not directly saved in db
        em.detach(updatedCartelera);
        updatedCartelera
            .dia(UPDATED_DIA)
            .cine(UPDATED_CINE)
            .pelicula(UPDATED_PELICULA)
            .sala(UPDATED_SALA);

        restCarteleraMockMvc.perform(put("/api/carteleras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCartelera)))
            .andExpect(status().isOk());

        // Validate the Cartelera in the database
        List<Cartelera> carteleraList = carteleraRepository.findAll();
        assertThat(carteleraList).hasSize(databaseSizeBeforeUpdate);
        Cartelera testCartelera = carteleraList.get(carteleraList.size() - 1);
        assertThat(testCartelera.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testCartelera.getCine()).isEqualTo(UPDATED_CINE);
        assertThat(testCartelera.getPelicula()).isEqualTo(UPDATED_PELICULA);
        assertThat(testCartelera.getSala()).isEqualTo(UPDATED_SALA);
    }

    @Test
    @Transactional
    public void updateNonExistingCartelera() throws Exception {
        int databaseSizeBeforeUpdate = carteleraRepository.findAll().size();

        // Create the Cartelera

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCarteleraMockMvc.perform(put("/api/carteleras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartelera)))
            .andExpect(status().isCreated());

        // Validate the Cartelera in the database
        List<Cartelera> carteleraList = carteleraRepository.findAll();
        assertThat(carteleraList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCartelera() throws Exception {
        // Initialize the database
        carteleraRepository.saveAndFlush(cartelera);
        int databaseSizeBeforeDelete = carteleraRepository.findAll().size();

        // Get the cartelera
        restCarteleraMockMvc.perform(delete("/api/carteleras/{id}", cartelera.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cartelera> carteleraList = carteleraRepository.findAll();
        assertThat(carteleraList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cartelera.class);
        Cartelera cartelera1 = new Cartelera();
        cartelera1.setId(1L);
        Cartelera cartelera2 = new Cartelera();
        cartelera2.setId(cartelera1.getId());
        assertThat(cartelera1).isEqualTo(cartelera2);
        cartelera2.setId(2L);
        assertThat(cartelera1).isNotEqualTo(cartelera2);
        cartelera1.setId(null);
        assertThat(cartelera1).isNotEqualTo(cartelera2);
    }
}
