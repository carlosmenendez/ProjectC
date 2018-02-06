package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BCiNeApp;

import com.mycompany.myapp.domain.Valoracion;
import com.mycompany.myapp.repository.ValoracionRepository;
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
 * Test class for the ValoracionResource REST controller.
 *
 * @see ValoracionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BCiNeApp.class)
public class ValoracionResourceIntTest {

    private static final Double DEFAULT_NOTA = 1D;
    private static final Double UPDATED_NOTA = 2D;

    private static final Integer DEFAULT_USUARIO_VOTADO = 1;
    private static final Integer UPDATED_USUARIO_VOTADO = 2;

    private static final Integer DEFAULT_PELICULA_VOTADA = 1;
    private static final Integer UPDATED_PELICULA_VOTADA = 2;

    @Autowired
    private ValoracionRepository valoracionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restValoracionMockMvc;

    private Valoracion valoracion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ValoracionResource valoracionResource = new ValoracionResource(valoracionRepository);
        this.restValoracionMockMvc = MockMvcBuilders.standaloneSetup(valoracionResource)
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
    public static Valoracion createEntity(EntityManager em) {
        Valoracion valoracion = new Valoracion()
            .nota(DEFAULT_NOTA)
            .usuarioVotado(DEFAULT_USUARIO_VOTADO)
            .peliculaVotada(DEFAULT_PELICULA_VOTADA);
        return valoracion;
    }

    @Before
    public void initTest() {
        valoracion = createEntity(em);
    }

    @Test
    @Transactional
    public void createValoracion() throws Exception {
        int databaseSizeBeforeCreate = valoracionRepository.findAll().size();

        // Create the Valoracion
        restValoracionMockMvc.perform(post("/api/valoracions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valoracion)))
            .andExpect(status().isCreated());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeCreate + 1);
        Valoracion testValoracion = valoracionList.get(valoracionList.size() - 1);
        assertThat(testValoracion.getNota()).isEqualTo(DEFAULT_NOTA);
        assertThat(testValoracion.getUsuarioVotado()).isEqualTo(DEFAULT_USUARIO_VOTADO);
        assertThat(testValoracion.getPeliculaVotada()).isEqualTo(DEFAULT_PELICULA_VOTADA);
    }

    @Test
    @Transactional
    public void createValoracionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = valoracionRepository.findAll().size();

        // Create the Valoracion with an existing ID
        valoracion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restValoracionMockMvc.perform(post("/api/valoracions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valoracion)))
            .andExpect(status().isBadRequest());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllValoracions() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList
        restValoracionMockMvc.perform(get("/api/valoracions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valoracion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA.doubleValue())))
            .andExpect(jsonPath("$.[*].usuarioVotado").value(hasItem(DEFAULT_USUARIO_VOTADO)))
            .andExpect(jsonPath("$.[*].peliculaVotada").value(hasItem(DEFAULT_PELICULA_VOTADA)));
    }

    @Test
    @Transactional
    public void getValoracion() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get the valoracion
        restValoracionMockMvc.perform(get("/api/valoracions/{id}", valoracion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(valoracion.getId().intValue()))
            .andExpect(jsonPath("$.nota").value(DEFAULT_NOTA.doubleValue()))
            .andExpect(jsonPath("$.usuarioVotado").value(DEFAULT_USUARIO_VOTADO))
            .andExpect(jsonPath("$.peliculaVotada").value(DEFAULT_PELICULA_VOTADA));
    }

    @Test
    @Transactional
    public void getNonExistingValoracion() throws Exception {
        // Get the valoracion
        restValoracionMockMvc.perform(get("/api/valoracions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValoracion() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);
        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();

        // Update the valoracion
        Valoracion updatedValoracion = valoracionRepository.findOne(valoracion.getId());
        // Disconnect from session so that the updates on updatedValoracion are not directly saved in db
        em.detach(updatedValoracion);
        updatedValoracion
            .nota(UPDATED_NOTA)
            .usuarioVotado(UPDATED_USUARIO_VOTADO)
            .peliculaVotada(UPDATED_PELICULA_VOTADA);

        restValoracionMockMvc.perform(put("/api/valoracions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedValoracion)))
            .andExpect(status().isOk());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate);
        Valoracion testValoracion = valoracionList.get(valoracionList.size() - 1);
        assertThat(testValoracion.getNota()).isEqualTo(UPDATED_NOTA);
        assertThat(testValoracion.getUsuarioVotado()).isEqualTo(UPDATED_USUARIO_VOTADO);
        assertThat(testValoracion.getPeliculaVotada()).isEqualTo(UPDATED_PELICULA_VOTADA);
    }

    @Test
    @Transactional
    public void updateNonExistingValoracion() throws Exception {
        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();

        // Create the Valoracion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restValoracionMockMvc.perform(put("/api/valoracions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valoracion)))
            .andExpect(status().isCreated());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteValoracion() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);
        int databaseSizeBeforeDelete = valoracionRepository.findAll().size();

        // Get the valoracion
        restValoracionMockMvc.perform(delete("/api/valoracions/{id}", valoracion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Valoracion.class);
        Valoracion valoracion1 = new Valoracion();
        valoracion1.setId(1L);
        Valoracion valoracion2 = new Valoracion();
        valoracion2.setId(valoracion1.getId());
        assertThat(valoracion1).isEqualTo(valoracion2);
        valoracion2.setId(2L);
        assertThat(valoracion1).isNotEqualTo(valoracion2);
        valoracion1.setId(null);
        assertThat(valoracion1).isNotEqualTo(valoracion2);
    }
}
