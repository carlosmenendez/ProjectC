package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Cine;

import com.mycompany.myapp.repository.CineRepository;
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
 * REST controller for managing Cine.
 */
@RestController
@RequestMapping("/api")
public class CineResource {

    private final Logger log = LoggerFactory.getLogger(CineResource.class);

    private static final String ENTITY_NAME = "cine";

    private final CineRepository cineRepository;

    public CineResource(CineRepository cineRepository) {
        this.cineRepository = cineRepository;
    }

    /**
     * POST  /cines : Create a new cine.
     *
     * @param cine the cine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cine, or with status 400 (Bad Request) if the cine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cines")
    @Timed
    public ResponseEntity<Cine> createCine(@RequestBody Cine cine) throws URISyntaxException {
        log.debug("REST request to save Cine : {}", cine);
        if (cine.getId() != null) {
            throw new BadRequestAlertException("A new cine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cine result = cineRepository.save(cine);
        return ResponseEntity.created(new URI("/api/cines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cines : Updates an existing cine.
     *
     * @param cine the cine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cine,
     * or with status 400 (Bad Request) if the cine is not valid,
     * or with status 500 (Internal Server Error) if the cine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cines")
    @Timed
    public ResponseEntity<Cine> updateCine(@RequestBody Cine cine) throws URISyntaxException {
        log.debug("REST request to update Cine : {}", cine);
        if (cine.getId() == null) {
            return createCine(cine);
        }
        Cine result = cineRepository.save(cine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cines : get all the cines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cines in body
     */
    @GetMapping("/cines")
    @Timed
    public List<Cine> getAllCines() {
        log.debug("REST request to get all Cines");
        return cineRepository.findAll();
        }

    /**
     * GET  /cines/:id : get the "id" cine.
     *
     * @param id the id of the cine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cine, or with status 404 (Not Found)
     */
    @GetMapping("/cines/{id}")
    @Timed
    public ResponseEntity<Cine> getCine(@PathVariable Long id) {
        log.debug("REST request to get Cine : {}", id);
        Cine cine = cineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cine));
    }

    /**
     * DELETE  /cines/:id : delete the "id" cine.
     *
     * @param id the id of the cine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cines/{id}")
    @Timed
    public ResponseEntity<Void> deleteCine(@PathVariable Long id) {
        log.debug("REST request to delete Cine : {}", id);
        cineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
