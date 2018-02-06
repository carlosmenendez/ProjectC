package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BCiNeApp;

import com.mycompany.myapp.domain.Artista;
import com.mycompany.myapp.repository.ArtistaRepository;
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
 * Test class for the ArtistaResource REST controller.
 *
 * @see ArtistaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BCiNeApp.class)
public class ArtistaResourceIntTest {

    private static final String DEFAULT_NOMBRE_COMPLETO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_COMPLETO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_NACIMIENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_NACIMIENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ES_DIRECTOR = false;
    private static final Boolean UPDATED_ES_DIRECTOR = true;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArtistaMockMvc;

    private Artista artista;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArtistaResource artistaResource = new ArtistaResource(artistaRepository);
        this.restArtistaMockMvc = MockMvcBuilders.standaloneSetup(artistaResource)
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
    public static Artista createEntity(EntityManager em) {
        Artista artista = new Artista()
            .nombreCompleto(DEFAULT_NOMBRE_COMPLETO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .esDirector(DEFAULT_ES_DIRECTOR);
        return artista;
    }

    @Before
    public void initTest() {
        artista = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtista() throws Exception {
        int databaseSizeBeforeCreate = artistaRepository.findAll().size();

        // Create the Artista
        restArtistaMockMvc.perform(post("/api/artistas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artista)))
            .andExpect(status().isCreated());

        // Validate the Artista in the database
        List<Artista> artistaList = artistaRepository.findAll();
        assertThat(artistaList).hasSize(databaseSizeBeforeCreate + 1);
        Artista testArtista = artistaList.get(artistaList.size() - 1);
        assertThat(testArtista.getNombreCompleto()).isEqualTo(DEFAULT_NOMBRE_COMPLETO);
        assertThat(testArtista.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testArtista.isEsDirector()).isEqualTo(DEFAULT_ES_DIRECTOR);
    }

    @Test
    @Transactional
    public void createArtistaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artistaRepository.findAll().size();

        // Create the Artista with an existing ID
        artista.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtistaMockMvc.perform(post("/api/artistas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artista)))
            .andExpect(status().isBadRequest());

        // Validate the Artista in the database
        List<Artista> artistaList = artistaRepository.findAll();
        assertThat(artistaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllArtistas() throws Exception {
        // Initialize the database
        artistaRepository.saveAndFlush(artista);

        // Get all the artistaList
        restArtistaMockMvc.perform(get("/api/artistas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreCompleto").value(hasItem(DEFAULT_NOMBRE_COMPLETO.toString())))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(sameInstant(DEFAULT_FECHA_NACIMIENTO))))
            .andExpect(jsonPath("$.[*].esDirector").value(hasItem(DEFAULT_ES_DIRECTOR.booleanValue())));
    }

    @Test
    @Transactional
    public void getArtista() throws Exception {
        // Initialize the database
        artistaRepository.saveAndFlush(artista);

        // Get the artista
        restArtistaMockMvc.perform(get("/api/artistas/{id}", artista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artista.getId().intValue()))
            .andExpect(jsonPath("$.nombreCompleto").value(DEFAULT_NOMBRE_COMPLETO.toString()))
            .andExpect(jsonPath("$.fechaNacimiento").value(sameInstant(DEFAULT_FECHA_NACIMIENTO)))
            .andExpect(jsonPath("$.esDirector").value(DEFAULT_ES_DIRECTOR.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingArtista() throws Exception {
        // Get the artista
        restArtistaMockMvc.perform(get("/api/artistas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtista() throws Exception {
        // Initialize the database
        artistaRepository.saveAndFlush(artista);
        int databaseSizeBeforeUpdate = artistaRepository.findAll().size();

        // Update the artista
        Artista updatedArtista = artistaRepository.findOne(artista.getId());
        // Disconnect from session so that the updates on updatedArtista are not directly saved in db
        em.detach(updatedArtista);
        updatedArtista
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .esDirector(UPDATED_ES_DIRECTOR);

        restArtistaMockMvc.perform(put("/api/artistas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArtista)))
            .andExpect(status().isOk());

        // Validate the Artista in the database
        List<Artista> artistaList = artistaRepository.findAll();
        assertThat(artistaList).hasSize(databaseSizeBeforeUpdate);
        Artista testArtista = artistaList.get(artistaList.size() - 1);
        assertThat(testArtista.getNombreCompleto()).isEqualTo(UPDATED_NOMBRE_COMPLETO);
        assertThat(testArtista.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testArtista.isEsDirector()).isEqualTo(UPDATED_ES_DIRECTOR);
    }

    @Test
    @Transactional
    public void updateNonExistingArtista() throws Exception {
        int databaseSizeBeforeUpdate = artistaRepository.findAll().size();

        // Create the Artista

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restArtistaMockMvc.perform(put("/api/artistas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artista)))
            .andExpect(status().isCreated());

        // Validate the Artista in the database
        List<Artista> artistaList = artistaRepository.findAll();
        assertThat(artistaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteArtista() throws Exception {
        // Initialize the database
        artistaRepository.saveAndFlush(artista);
        int databaseSizeBeforeDelete = artistaRepository.findAll().size();

        // Get the artista
        restArtistaMockMvc.perform(delete("/api/artistas/{id}", artista.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Artista> artistaList = artistaRepository.findAll();
        assertThat(artistaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Artista.class);
        Artista artista1 = new Artista();
        artista1.setId(1L);
        Artista artista2 = new Artista();
        artista2.setId(artista1.getId());
        assertThat(artista1).isEqualTo(artista2);
        artista2.setId(2L);
        assertThat(artista1).isNotEqualTo(artista2);
        artista1.setId(null);
        assertThat(artista1).isNotEqualTo(artista2);
    }
}
