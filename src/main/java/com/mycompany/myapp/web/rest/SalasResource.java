package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Salas;

import com.mycompany.myapp.repository.SalasRepository;
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
 * REST controller for managing Salas.
 */
@RestController
@RequestMapping("/api")
public class SalasResource {

    private final Logger log = LoggerFactory.getLogger(SalasResource.class);

    private static final String ENTITY_NAME = "salas";

    private final SalasRepository salasRepository;

    public SalasResource(SalasRepository salasRepository) {
        this.salasRepository = salasRepository;
    }

    /**
     * POST  /salas : Create a new salas.
     *
     * @param salas the salas to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salas, or with status 400 (Bad Request) if the salas has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/salas")
    @Timed
    public ResponseEntity<Salas> createSalas(@RequestBody Salas salas) throws URISyntaxException {
        log.debug("REST request to save Salas : {}", salas);
        if (salas.getId() != null) {
            throw new BadRequestAlertException("A new salas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Salas result = salasRepository.save(salas);
        return ResponseEntity.created(new URI("/api/salas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /salas : Updates an existing salas.
     *
     * @param salas the salas to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salas,
     * or with status 400 (Bad Request) if the salas is not valid,
     * or with status 500 (Internal Server Error) if the salas couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/salas")
    @Timed
    public ResponseEntity<Salas> updateSalas(@RequestBody Salas salas) throws URISyntaxException {
        log.debug("REST request to update Salas : {}", salas);
        if (salas.getId() == null) {
            return createSalas(salas);
        }
        Salas result = salasRepository.save(salas);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salas.getId().toString()))
            .body(result);
    }

    /**
     * GET  /salas : get all the salas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of salas in body
     */
    @GetMapping("/salas")
    @Timed
    public List<Salas> getAllSalas() {
        log.debug("REST request to get all Salas");
        return salasRepository.findAll();
        }

    /**
     * GET  /salas/:id : get the "id" salas.
     *
     * @param id the id of the salas to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salas, or with status 404 (Not Found)
     */
    @GetMapping("/salas/{id}")
    @Timed
    public ResponseEntity<Salas> getSalas(@PathVariable Long id) {
        log.debug("REST request to get Salas : {}", id);
        Salas salas = salasRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salas));
    }

    /**
     * DELETE  /salas/:id : delete the "id" salas.
     *
     * @param id the id of the salas to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/salas/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalas(@PathVariable Long id) {
        log.debug("REST request to delete Salas : {}", id);
        salasRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
