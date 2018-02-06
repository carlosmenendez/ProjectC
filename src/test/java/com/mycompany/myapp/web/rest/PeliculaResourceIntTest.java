package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BCiNeApp;

import com.mycompany.myapp.domain.Pelicula;
import com.mycompany.myapp.repository.PeliculaRepository;
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
 * Test class for the PeliculaResource REST controller.
 *
 * @see PeliculaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BCiNeApp.class)
public class PeliculaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PAIS = "AAAAAAAAAA";
    private static final String UPDATED_PAIS = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURACION = 1;
    private static final Integer UPDATED_DURACION = 2;

    private static final ZonedDateTime DEFAULT_FECHA_DE_ESTRENO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_DE_ESTRENO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_GENERO = "AAAAAAAAAA";
    private static final String UPDATED_GENERO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ES_ESTRENO = false;
    private static final Boolean UPDATED_ES_ESTRENO = true;

    private static final String DEFAULT_SINOPSIS = "AAAAAAAAAA";
    private static final String UPDATED_SINOPSIS = "BBBBBBBBBB";

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeliculaMockMvc;

    private Pelicula pelicula;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PeliculaResource peliculaResource = new PeliculaResource(peliculaRepository);
        this.restPeliculaMockMvc = MockMvcBuilders.standaloneSetup(peliculaResource)
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
    public static Pelicula createEntity(EntityManager em) {
        Pelicula pelicula = new Pelicula()
            .nombre(DEFAULT_NOMBRE)
            .pais(DEFAULT_PAIS)
            .duracion(DEFAULT_DURACION)
            .fechaDeEstreno(DEFAULT_FECHA_DE_ESTRENO)
            .genero(DEFAULT_GENERO)
            .esEstreno(DEFAULT_ES_ESTRENO)
            .sinopsis(DEFAULT_SINOPSIS);
        return pelicula;
    }

    @Before
    public void initTest() {
        pelicula = createEntity(em);
    }

    @Test
    @Transactional
    public void createPelicula() throws Exception {
        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();

        // Create the Pelicula
        restPeliculaMockMvc.perform(post("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isCreated());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate + 1);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPelicula.getPais()).isEqualTo(DEFAULT_PAIS);
        assertThat(testPelicula.getDuracion()).isEqualTo(DEFAULT_DURACION);
        assertThat(testPelicula.getFechaDeEstreno()).isEqualTo(DEFAULT_FECHA_DE_ESTRENO);
        assertThat(testPelicula.getGenero()).isEqualTo(DEFAULT_GENERO);
        assertThat(testPelicula.isEsEstreno()).isEqualTo(DEFAULT_ES_ESTRENO);
        assertThat(testPelicula.getSinopsis()).isEqualTo(DEFAULT_SINOPSIS);
    }

    @Test
    @Transactional
    public void createPeliculaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();

        // Create the Pelicula with an existing ID
        pelicula.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeliculaMockMvc.perform(post("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isBadRequest());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPeliculas() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList
        restPeliculaMockMvc.perform(get("/api/peliculas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pelicula.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS.toString())))
            .andExpect(jsonPath("$.[*].duracion").value(hasItem(DEFAULT_DURACION)))
            .andExpect(jsonPath("$.[*].fechaDeEstreno").value(hasItem(sameInstant(DEFAULT_FECHA_DE_ESTRENO))))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())))
            .andExpect(jsonPath("$.[*].esEstreno").value(hasItem(DEFAULT_ES_ESTRENO.booleanValue())))
            .andExpect(jsonPath("$.[*].sinopsis").value(hasItem(DEFAULT_SINOPSIS.toString())));
    }

    @Test
    @Transactional
    public void getPelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get the pelicula
        restPeliculaMockMvc.perform(get("/api/peliculas/{id}", pelicula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pelicula.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.pais").value(DEFAULT_PAIS.toString()))
            .andExpect(jsonPath("$.duracion").value(DEFAULT_DURACION))
            .andExpect(jsonPath("$.fechaDeEstreno").value(sameInstant(DEFAULT_FECHA_DE_ESTRENO)))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO.toString()))
            .andExpect(jsonPath("$.esEstreno").value(DEFAULT_ES_ESTRENO.booleanValue()))
            .andExpect(jsonPath("$.sinopsis").value(DEFAULT_SINOPSIS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPelicula() throws Exception {
        // Get the pelicula
        restPeliculaMockMvc.perform(get("/api/peliculas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Update the pelicula
        Pelicula updatedPelicula = peliculaRepository.findOne(pelicula.getId());
        // Disconnect from session so that the updates on updatedPelicula are not directly saved in db
        em.detach(updatedPelicula);
        updatedPelicula
            .nombre(UPDATED_NOMBRE)
            .pais(UPDATED_PAIS)
            .duracion(UPDATED_DURACION)
            .fechaDeEstreno(UPDATED_FECHA_DE_ESTRENO)
            .genero(UPDATED_GENERO)
            .esEstreno(UPDATED_ES_ESTRENO)
            .sinopsis(UPDATED_SINOPSIS);

        restPeliculaMockMvc.perform(put("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPelicula)))
            .andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPelicula.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testPelicula.getDuracion()).isEqualTo(UPDATED_DURACION);
        assertThat(testPelicula.getFechaDeEstreno()).isEqualTo(UPDATED_FECHA_DE_ESTRENO);
        assertThat(testPelicula.getGenero()).isEqualTo(UPDATED_GENERO);
        assertThat(testPelicula.isEsEstreno()).isEqualTo(UPDATED_ES_ESTRENO);
        assertThat(testPelicula.getSinopsis()).isEqualTo(UPDATED_SINOPSIS);
    }

    @Test
    @Transactional
    public void updateNonExistingPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Create the Pelicula

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPeliculaMockMvc.perform(put("/api/peliculas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pelicula)))
            .andExpect(status().isCreated());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);
        int databaseSizeBeforeDelete = peliculaRepository.findAll().size();

        // Get the pelicula
        restPeliculaMockMvc.perform(delete("/api/peliculas/{id}", pelicula.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pelicula.class);
        Pelicula pelicula1 = new Pelicula();
        pelicula1.setId(1L);
        Pelicula pelicula2 = new Pelicula();
        pelicula2.setId(pelicula1.getId());
        assertThat(pelicula1).isEqualTo(pelicula2);
        pelicula2.setId(2L);
        assertThat(pelicula1).isNotEqualTo(pelicula2);
        pelicula1.setId(null);
        assertThat(pelicula1).isNotEqualTo(pelicula2);
    }
}
