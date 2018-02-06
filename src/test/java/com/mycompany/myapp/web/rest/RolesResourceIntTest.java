package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BCiNeApp;

import com.mycompany.myapp.domain.Roles;
import com.mycompany.myapp.repository.RolesRepository;
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
 * Test class for the RolesResource REST controller.
 *
 * @see RolesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BCiNeApp.class)
public class RolesResourceIntTest {

    private static final String DEFAULT_NOMBRE_ROL = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_ROL = "BBBBBBBBBB";

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRolesMockMvc;

    private Roles roles;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RolesResource rolesResource = new RolesResource(rolesRepository);
        this.restRolesMockMvc = MockMvcBuilders.standaloneSetup(rolesResource)
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
    public static Roles createEntity(EntityManager em) {
        Roles roles = new Roles()
            .nombreRol(DEFAULT_NOMBRE_ROL);
        return roles;
    }

    @Before
    public void initTest() {
        roles = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoles() throws Exception {
        int databaseSizeBeforeCreate = rolesRepository.findAll().size();

        // Create the Roles
        restRolesMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roles)))
            .andExpect(status().isCreated());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeCreate + 1);
        Roles testRoles = rolesList.get(rolesList.size() - 1);
        assertThat(testRoles.getNombreRol()).isEqualTo(DEFAULT_NOMBRE_ROL);
    }

    @Test
    @Transactional
    public void createRolesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rolesRepository.findAll().size();

        // Create the Roles with an existing ID
        roles.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRolesMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roles)))
            .andExpect(status().isBadRequest());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRoles() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get all the rolesList
        restRolesMockMvc.perform(get("/api/roles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roles.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreRol").value(hasItem(DEFAULT_NOMBRE_ROL.toString())));
    }

    @Test
    @Transactional
    public void getRoles() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);

        // Get the roles
        restRolesMockMvc.perform(get("/api/roles/{id}", roles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roles.getId().intValue()))
            .andExpect(jsonPath("$.nombreRol").value(DEFAULT_NOMBRE_ROL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoles() throws Exception {
        // Get the roles
        restRolesMockMvc.perform(get("/api/roles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoles() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);
        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();

        // Update the roles
        Roles updatedRoles = rolesRepository.findOne(roles.getId());
        // Disconnect from session so that the updates on updatedRoles are not directly saved in db
        em.detach(updatedRoles);
        updatedRoles
            .nombreRol(UPDATED_NOMBRE_ROL);

        restRolesMockMvc.perform(put("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRoles)))
            .andExpect(status().isOk());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
        Roles testRoles = rolesList.get(rolesList.size() - 1);
        assertThat(testRoles.getNombreRol()).isEqualTo(UPDATED_NOMBRE_ROL);
    }

    @Test
    @Transactional
    public void updateNonExistingRoles() throws Exception {
        int databaseSizeBeforeUpdate = rolesRepository.findAll().size();

        // Create the Roles

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRolesMockMvc.perform(put("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roles)))
            .andExpect(status().isCreated());

        // Validate the Roles in the database
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRoles() throws Exception {
        // Initialize the database
        rolesRepository.saveAndFlush(roles);
        int databaseSizeBeforeDelete = rolesRepository.findAll().size();

        // Get the roles
        restRolesMockMvc.perform(delete("/api/roles/{id}", roles.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Roles> rolesList = rolesRepository.findAll();
        assertThat(rolesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Roles.class);
        Roles roles1 = new Roles();
        roles1.setId(1L);
        Roles roles2 = new Roles();
        roles2.setId(roles1.getId());
        assertThat(roles1).isEqualTo(roles2);
        roles2.setId(2L);
        assertThat(roles1).isNotEqualTo(roles2);
        roles1.setId(null);
        assertThat(roles1).isNotEqualTo(roles2);
    }
}
