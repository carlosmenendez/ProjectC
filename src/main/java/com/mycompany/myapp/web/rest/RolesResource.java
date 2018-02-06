package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Roles;

import com.mycompany.myapp.repository.RolesRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Roles.
 */
@RestController
@RequestMapping("/api")
public class RolesResource {

    private final Logger log = LoggerFactory.getLogger(RolesResource.class);

    private static final String ENTITY_NAME = "roles";

    private final RolesRepository rolesRepository;

    public RolesResource(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    /**
     * POST  /roles : Create a new roles.
     *
     * @param roles the roles to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roles, or with status 400 (Bad Request) if the roles has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/roles")
    @Timed
    public ResponseEntity<Roles> createRoles(@RequestBody Roles roles) throws URISyntaxException {
        log.debug("REST request to save Roles : {}", roles);
        if (roles.getId() != null) {
            throw new BadRequestAlertException("A new roles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Roles result = rolesRepository.save(roles);
        return ResponseEntity.created(new URI("/api/roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /roles : Updates an existing roles.
     *
     * @param roles the roles to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roles,
     * or with status 400 (Bad Request) if the roles is not valid,
     * or with status 500 (Internal Server Error) if the roles couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles")
    @Timed
    public ResponseEntity<Roles> updateRoles(@RequestBody Roles roles) throws URISyntaxException {
        log.debug("REST request to update Roles : {}", roles);
        if (roles.getId() == null) {
            return createRoles(roles);
        }
        Roles result = rolesRepository.save(roles);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roles.getId().toString()))
            .body(result);
    }

    /**
     * GET  /roles : get all the roles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/roles")
    @Timed
    public List<Roles> getAllRoles() {
        log.debug("REST request to get all Roles");
        return rolesRepository.findAll();
        }

    /**
     * GET  /roles/:id : get the "id" roles.
     *
     * @param id the id of the roles to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roles, or with status 404 (Not Found)
     */
    @GetMapping("/roles/{id}")
    @Timed
    public ResponseEntity<Roles> getRoles(@PathVariable Long id) {
        log.debug("REST request to get Roles : {}", id);
        Roles roles = rolesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(roles));
    }

    /**
     * DELETE  /roles/:id : delete the "id" roles.
     *
     * @param id the id of the roles to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/roles/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoles(@PathVariable Long id) {
        log.debug("REST request to delete Roles : {}", id);
        rolesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
