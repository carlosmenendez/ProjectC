package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BCiNeApp;

import com.mycompany.myapp.domain.ComentariosCine;
import com.mycompany.myapp.repository.ComentariosCineRepository;
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
 * Test class for the ComentariosCineResource REST controller.
 *
 * @see ComentariosCineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BCiNeApp.class)
public class ComentariosCineResourceIntTest {

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final Integer DEFAULT_USUARIO = 1;
    private static final Integer UPDATED_USUARIO = 2;

    private static final Integer DEFAULT_CINE = 1;
    private static final Integer UPDATED_CINE = 2;

    @Autowired
    private ComentariosCineRepository comentariosCineRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restComentariosCineMockMvc;

    private ComentariosCine comentariosCine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ComentariosCineResource comentariosCineResource = new ComentariosCineResource(comentariosCineRepository);
        this.restComentariosCineMockMvc = MockMvcBuilders.standaloneSetup(comentariosCineResource)
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
    public static ComentariosCine createEntity(EntityManager em) {
        ComentariosCine comentariosCine = new ComentariosCine()
            .comentario(DEFAULT_COMENTARIO)
            .usuario(DEFAULT_USUARIO)
            .cine(DEFAULT_CINE);
        return comentariosCine;
    }

    @Before
    public void initTest() {
        comentariosCine = createEntity(em);
    }

    @Test
    @Transactional
    public void createComentariosCine() throws Exception {
        int databaseSizeBeforeCreate = comentariosCineRepository.findAll().size();

        // Create the ComentariosCine
        restComentariosCineMockMvc.perform(post("/api/comentarios-cines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentariosCine)))
            .andExpect(status().isCreated());

        // Validate the ComentariosCine in the database
        List<ComentariosCine> comentariosCineList = comentariosCineRepository.findAll();
        assertThat(comentariosCineList).hasSize(databaseSizeBeforeCreate + 1);
        ComentariosCine testComentariosCine = comentariosCineList.get(comentariosCineList.size() - 1);
        assertThat(testComentariosCine.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testComentariosCine.getUsuario()).isEqualTo(DEFAULT_USUARIO);
        assertThat(testComentariosCine.getCine()).isEqualTo(DEFAULT_CINE);
    }

    @Test
    @Transactional
    public void createComentariosCineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = comentariosCineRepository.findAll().size();

        // Create the ComentariosCine with an existing ID
        comentariosCine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComentariosCineMockMvc.perform(post("/api/comentarios-cines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentariosCine)))
            .andExpect(status().isBadRequest());

        // Validate the ComentariosCine in the database
        List<ComentariosCine> comentariosCineList = comentariosCineRepository.findAll();
        assertThat(comentariosCineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllComentariosCines() throws Exception {
        // Initialize the database
        comentariosCineRepository.saveAndFlush(comentariosCine);

        // Get all the comentariosCineList
        restComentariosCineMockMvc.perform(get("/api/comentarios-cines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comentariosCine.getId().intValue())))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO.toString())))
            .andExpect(jsonPath("$.[*].usuario").value(hasItem(DEFAULT_USUARIO)))
            .andExpect(jsonPath("$.[*].cine").value(hasItem(DEFAULT_CINE)));
    }

    @Test
    @Transactional
    public void getComentariosCine() throws Exception {
        // Initialize the database
        comentariosCineRepository.saveAndFlush(comentariosCine);

        // Get the comentariosCine
        restComentariosCineMockMvc.perform(get("/api/comentarios-cines/{id}", comentariosCine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(comentariosCine.getId().intValue()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO.toString()))
            .andExpect(jsonPath("$.usuario").value(DEFAULT_USUARIO))
            .andExpect(jsonPath("$.cine").value(DEFAULT_CINE));
    }

    @Test
    @Transactional
    public void getNonExistingComentariosCine() throws Exception {
        // Get the comentariosCine
        restComentariosCineMockMvc.perform(get("/api/comentarios-cines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComentariosCine() throws Exception {
        // Initialize the database
        comentariosCineRepository.saveAndFlush(comentariosCine);
        int databaseSizeBeforeUpdate = comentariosCineRepository.findAll().size();

        // Update the comentariosCine
        ComentariosCine updatedComentariosCine = comentariosCineRepository.findOne(comentariosCine.getId());
        // Disconnect from session so that the updates on updatedComentariosCine are not directly saved in db
        em.detach(updatedComentariosCine);
        updatedComentariosCine
            .comentario(UPDATED_COMENTARIO)
            .usuario(UPDATED_USUARIO)
            .cine(UPDATED_CINE);

        restComentariosCineMockMvc.perform(put("/api/comentarios-cines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedComentariosCine)))
            .andExpect(status().isOk());

        // Validate the ComentariosCine in the database
        List<ComentariosCine> comentariosCineList = comentariosCineRepository.findAll();
        assertThat(comentariosCineList).hasSize(databaseSizeBeforeUpdate);
        ComentariosCine testComentariosCine = comentariosCineList.get(comentariosCineList.size() - 1);
        assertThat(testComentariosCine.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testComentariosCine.getUsuario()).isEqualTo(UPDATED_USUARIO);
        assertThat(testComentariosCine.getCine()).isEqualTo(UPDATED_CINE);
    }

    @Test
    @Transactional
    public void updateNonExistingComentariosCine() throws Exception {
        int databaseSizeBeforeUpdate = comentariosCineRepository.findAll().size();

        // Create the ComentariosCine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restComentariosCineMockMvc.perform(put("/api/comentarios-cines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentariosCine)))
            .andExpect(status().isCreated());

        // Validate the ComentariosCine in the database
        List<ComentariosCine> comentariosCineList = comentariosCineRepository.findAll();
        assertThat(comentariosCineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteComentariosCine() throws Exception {
        // Initialize the database
        comentariosCineRepository.saveAndFlush(comentariosCine);
        int databaseSizeBeforeDelete = comentariosCineRepository.findAll().size();

        // Get the comentariosCine
        restComentariosCineMockMvc.perform(delete("/api/comentarios-cines/{id}", comentariosCine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ComentariosCine> comentariosCineList = comentariosCineRepository.findAll();
        assertThat(comentariosCineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComentariosCine.class);
        ComentariosCine comentariosCine1 = new ComentariosCine();
        comentariosCine1.setId(1L);
        ComentariosCine comentariosCine2 = new ComentariosCine();
        comentariosCine2.setId(comentariosCine1.getId());
        assertThat(comentariosCine1).isEqualTo(comentariosCine2);
        comentariosCine2.setId(2L);
        assertThat(comentariosCine1).isNotEqualTo(comentariosCine2);
        comentariosCine1.setId(null);
        assertThat(comentariosCine1).isNotEqualTo(comentariosCine2);
    }
}
