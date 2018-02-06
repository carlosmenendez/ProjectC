package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.ComentariosCine;

import com.mycompany.myapp.repository.ComentariosCineRepository;
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
 * REST controller for managing ComentariosCine.
 */
@RestController
@RequestMapping("/api")
public class ComentariosCineResource {

    private final Logger log = LoggerFactory.getLogger(ComentariosCineResource.class);

    private static final String ENTITY_NAME = "comentariosCine";

    private final ComentariosCineRepository comentariosCineRepository;

    public ComentariosCineResource(ComentariosCineRepository comentariosCineRepository) {
        this.comentariosCineRepository = comentariosCineRepository;
    }

    /**
     * POST  /comentarios-cines : Create a new comentariosCine.
     *
     * @param comentariosCine the comentariosCine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new comentariosCine, or with status 400 (Bad Request) if the comentariosCine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comentarios-cines")
    @Timed
    public ResponseEntity<ComentariosCine> createComentariosCine(@RequestBody ComentariosCine comentariosCine) throws URISyntaxException {
        log.debug("REST request to save ComentariosCine : {}", comentariosCine);
        if (comentariosCine.getId() != null) {
            throw new BadRequestAlertException("A new comentariosCine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComentariosCine result = comentariosCineRepository.save(comentariosCine);
        return ResponseEntity.created(new URI("/api/comentarios-cines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comentarios-cines : Updates an existing comentariosCine.
     *
     * @param comentariosCine the comentariosCine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated comentariosCine,
     * or with status 400 (Bad Request) if the comentariosCine is not valid,
     * or with status 500 (Internal Server Error) if the comentariosCine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comentarios-cines")
    @Timed
    public ResponseEntity<ComentariosCine> updateComentariosCine(@RequestBody ComentariosCine comentariosCine) throws URISyntaxException {
        log.debug("REST request to update ComentariosCine : {}", comentariosCine);
        if (comentariosCine.getId() == null) {
            return createComentariosCine(comentariosCine);
        }
        ComentariosCine result = comentariosCineRepository.save(comentariosCine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, comentariosCine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comentarios-cines : get all the comentariosCines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of comentariosCines in body
     */
    @GetMapping("/comentarios-cines")
    @Timed
    public List<ComentariosCine> getAllComentariosCines() {
        log.debug("REST request to get all ComentariosCines");
        return comentariosCineRepository.findAll();
        }

    /**
     * GET  /comentarios-cines/:id : get the "id" comentariosCine.
     *
     * @param id the id of the comentariosCine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the comentariosCine, or with status 404 (Not Found)
     */
    @GetMapping("/comentarios-cines/{id}")
    @Timed
    public ResponseEntity<ComentariosCine> getComentariosCine(@PathVariable Long id) {
        log.debug("REST request to get ComentariosCine : {}", id);
        ComentariosCine comentariosCine = comentariosCineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(comentariosCine));
    }

    /**
     * DELETE  /comentarios-cines/:id : delete the "id" comentariosCine.
     *
     * @param id the id of the comentariosCine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comentarios-cines/{id}")
    @Timed
    public ResponseEntity<Void> deleteComentariosCine(@PathVariable Long id) {
        log.debug("REST request to delete ComentariosCine : {}", id);
        comentariosCineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
