package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.ComentariosPeli;

import com.mycompany.myapp.repository.ComentariosPeliRepository;
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
 * REST controller for managing ComentariosPeli.
 */
@RestController
@RequestMapping("/api")
public class ComentariosPeliResource {

    private final Logger log = LoggerFactory.getLogger(ComentariosPeliResource.class);

    private static final String ENTITY_NAME = "comentariosPeli";

    private final ComentariosPeliRepository comentariosPeliRepository;

    public ComentariosPeliResource(ComentariosPeliRepository comentariosPeliRepository) {
        this.comentariosPeliRepository = comentariosPeliRepository;
    }

    /**
     * POST  /comentarios-pelis : Create a new comentariosPeli.
     *
     * @param comentariosPeli the comentariosPeli to create
     * @return the ResponseEntity with status 201 (Created) and with body the new comentariosPeli, or with status 400 (Bad Request) if the comentariosPeli has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comentarios-pelis")
    @Timed
    public ResponseEntity<ComentariosPeli> createComentariosPeli(@RequestBody ComentariosPeli comentariosPeli) throws URISyntaxException {
        log.debug("REST request to save ComentariosPeli : {}", comentariosPeli);
        if (comentariosPeli.getId() != null) {
            throw new BadRequestAlertException("A new comentariosPeli cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComentariosPeli result = comentariosPeliRepository.save(comentariosPeli);
        return ResponseEntity.created(new URI("/api/comentarios-pelis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comentarios-pelis : Updates an existing comentariosPeli.
     *
     * @param comentariosPeli the comentariosPeli to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated comentariosPeli,
     * or with status 400 (Bad Request) if the comentariosPeli is not valid,
     * or with status 500 (Internal Server Error) if the comentariosPeli couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comentarios-pelis")
    @Timed
    public ResponseEntity<ComentariosPeli> updateComentariosPeli(@RequestBody ComentariosPeli comentariosPeli) throws URISyntaxException {
        log.debug("REST request to update ComentariosPeli : {}", comentariosPeli);
        if (comentariosPeli.getId() == null) {
            return createComentariosPeli(comentariosPeli);
        }
        ComentariosPeli result = comentariosPeliRepository.save(comentariosPeli);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, comentariosPeli.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comentarios-pelis : get all the comentariosPelis.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of comentariosPelis in body
     */
    @GetMapping("/comentarios-pelis")
    @Timed
    public List<ComentariosPeli> getAllComentariosPelis() {
        log.debug("REST request to get all ComentariosPelis");
        return comentariosPeliRepository.findAll();
        }

    /**
     * GET  /comentarios-pelis/:id : get the "id" comentariosPeli.
     *
     * @param id the id of the comentariosPeli to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the comentariosPeli, or with status 404 (Not Found)
     */
    @GetMapping("/comentarios-pelis/{id}")
    @Timed
    public ResponseEntity<ComentariosPeli> getComentariosPeli(@PathVariable Long id) {
        log.debug("REST request to get ComentariosPeli : {}", id);
        ComentariosPeli comentariosPeli = comentariosPeliRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(comentariosPeli));
    }

    /**
     * DELETE  /comentarios-pelis/:id : delete the "id" comentariosPeli.
     *
     * @param id the id of the comentariosPeli to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comentarios-pelis/{id}")
    @Timed
    public ResponseEntity<Void> deleteComentariosPeli(@PathVariable Long id) {
        log.debug("REST request to delete ComentariosPeli : {}", id);
        comentariosPeliRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
