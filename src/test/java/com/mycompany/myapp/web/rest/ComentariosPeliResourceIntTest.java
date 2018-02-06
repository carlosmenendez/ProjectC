package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BCiNeApp;

import com.mycompany.myapp.domain.ComentariosPeli;
import com.mycompany.myapp.repository.ComentariosPeliRepository;
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
 * Test class for the ComentariosPeliResource REST controller.
 *
 * @see ComentariosPeliResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BCiNeApp.class)
public class ComentariosPeliResourceIntTest {

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final Integer DEFAULT_USUARIO = 1;
    private static final Integer UPDATED_USUARIO = 2;

    private static final Integer DEFAULT_PELI = 1;
    private static final Integer UPDATED_PELI = 2;

    @Autowired
    private ComentariosPeliRepository comentariosPeliRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restComentariosPeliMockMvc;

    private ComentariosPeli comentariosPeli;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ComentariosPeliResource comentariosPeliResource = new ComentariosPeliResource(comentariosPeliRepository);
        this.restComentariosPeliMockMvc = MockMvcBuilders.standaloneSetup(comentariosPeliResource)
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
    public static ComentariosPeli createEntity(EntityManager em) {
        ComentariosPeli comentariosPeli = new ComentariosPeli()
            .comentario(DEFAULT_COMENTARIO)
            .usuario(DEFAULT_USUARIO)
            .peli(DEFAULT_PELI);
        return comentariosPeli;
    }

    @Before
    public void initTest() {
        comentariosPeli = createEntity(em);
    }

    @Test
    @Transactional
    public void createComentariosPeli() throws Exception {
        int databaseSizeBeforeCreate = comentariosPeliRepository.findAll().size();

        // Create the ComentariosPeli
        restComentariosPeliMockMvc.perform(post("/api/comentarios-pelis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentariosPeli)))
            .andExpect(status().isCreated());

        // Validate the ComentariosPeli in the database
        List<ComentariosPeli> comentariosPeliList = comentariosPeliRepository.findAll();
        assertThat(comentariosPeliList).hasSize(databaseSizeBeforeCreate + 1);
        ComentariosPeli testComentariosPeli = comentariosPeliList.get(comentariosPeliList.size() - 1);
        assertThat(testComentariosPeli.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testComentariosPeli.getUsuario()).isEqualTo(DEFAULT_USUARIO);
        assertThat(testComentariosPeli.getPeli()).isEqualTo(DEFAULT_PELI);
    }

    @Test
    @Transactional
    public void createComentariosPeliWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = comentariosPeliRepository.findAll().size();

        // Create the ComentariosPeli with an existing ID
        comentariosPeli.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComentariosPeliMockMvc.perform(post("/api/comentarios-pelis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentariosPeli)))
            .andExpect(status().isBadRequest());

        // Validate the ComentariosPeli in the database
        List<ComentariosPeli> comentariosPeliList = comentariosPeliRepository.findAll();
        assertThat(comentariosPeliList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllComentariosPelis() throws Exception {
        // Initialize the database
        comentariosPeliRepository.saveAndFlush(comentariosPeli);

        // Get all the comentariosPeliList
        restComentariosPeliMockMvc.perform(get("/api/comentarios-pelis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comentariosPeli.getId().intValue())))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO.toString())))
            .andExpect(jsonPath("$.[*].usuario").value(hasItem(DEFAULT_USUARIO)))
            .andExpect(jsonPath("$.[*].peli").value(hasItem(DEFAULT_PELI)));
    }

    @Test
    @Transactional
    public void getComentariosPeli() throws Exception {
        // Initialize the database
        comentariosPeliRepository.saveAndFlush(comentariosPeli);

        // Get the comentariosPeli
        restComentariosPeliMockMvc.perform(get("/api/comentarios-pelis/{id}", comentariosPeli.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(comentariosPeli.getId().intValue()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO.toString()))
            .andExpect(jsonPath("$.usuario").value(DEFAULT_USUARIO))
            .andExpect(jsonPath("$.peli").value(DEFAULT_PELI));
    }

    @Test
    @Transactional
    public void getNonExistingComentariosPeli() throws Exception {
        // Get the comentariosPeli
        restComentariosPeliMockMvc.perform(get("/api/comentarios-pelis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComentariosPeli() throws Exception {
        // Initialize the database
        comentariosPeliRepository.saveAndFlush(comentariosPeli);
        int databaseSizeBeforeUpdate = comentariosPeliRepository.findAll().size();

        // Update the comentariosPeli
        ComentariosPeli updatedComentariosPeli = comentariosPeliRepository.findOne(comentariosPeli.getId());
        // Disconnect from session so that the updates on updatedComentariosPeli are not directly saved in db
        em.detach(updatedComentariosPeli);
        updatedComentariosPeli
            .comentario(UPDATED_COMENTARIO)
            .usuario(UPDATED_USUARIO)
            .peli(UPDATED_PELI);

        restComentariosPeliMockMvc.perform(put("/api/comentarios-pelis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedComentariosPeli)))
            .andExpect(status().isOk());

        // Validate the ComentariosPeli in the database
        List<ComentariosPeli> comentariosPeliList = comentariosPeliRepository.findAll();
        assertThat(comentariosPeliList).hasSize(databaseSizeBeforeUpdate);
        ComentariosPeli testComentariosPeli = comentariosPeliList.get(comentariosPeliList.size() - 1);
        assertThat(testComentariosPeli.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testComentariosPeli.getUsuario()).isEqualTo(UPDATED_USUARIO);
        assertThat(testComentariosPeli.getPeli()).isEqualTo(UPDATED_PELI);
    }

    @Test
    @Transactional
    public void updateNonExistingComentariosPeli() throws Exception {
        int databaseSizeBeforeUpdate = comentariosPeliRepository.findAll().size();

        // Create the ComentariosPeli

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restComentariosPeliMockMvc.perform(put("/api/comentarios-pelis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comentariosPeli)))
            .andExpect(status().isCreated());

        // Validate the ComentariosPeli in the database
        List<ComentariosPeli> comentariosPeliList = comentariosPeliRepository.findAll();
        assertThat(comentariosPeliList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteComentariosPeli() throws Exception {
        // Initialize the database
        comentariosPeliRepository.saveAndFlush(comentariosPeli);
        int databaseSizeBeforeDelete = comentariosPeliRepository.findAll().size();

        // Get the comentariosPeli
        restComentariosPeliMockMvc.perform(delete("/api/comentarios-pelis/{id}", comentariosPeli.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ComentariosPeli> comentariosPeliList = comentariosPeliRepository.findAll();
        assertThat(comentariosPeliList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComentariosPeli.class);
        ComentariosPeli comentariosPeli1 = new ComentariosPeli();
        comentariosPeli1.setId(1L);
        ComentariosPeli comentariosPeli2 = new ComentariosPeli();
        comentariosPeli2.setId(comentariosPeli1.getId());
        assertThat(comentariosPeli1).isEqualTo(comentariosPeli2);
        comentariosPeli2.setId(2L);
        assertThat(comentariosPeli1).isNotEqualTo(comentariosPeli2);
        comentariosPeli1.setId(null);
        assertThat(comentariosPeli1).isNotEqualTo(comentariosPeli2);
    }
}
