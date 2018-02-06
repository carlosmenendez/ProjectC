package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Valoracion;

import com.mycompany.myapp.repository.ValoracionRepository;
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
 * REST controller for managing Valoracion.
 */
@RestController
@RequestMapping("/api")
public class ValoracionResource {

    private final Logger log = LoggerFactory.getLogger(ValoracionResource.class);

    private static final String ENTITY_NAME = "valoracion";

    private final ValoracionRepository valoracionRepository;

    public ValoracionResource(ValoracionRepository valoracionRepository) {
        this.valoracionRepository = valoracionRepository;
    }

    /**
     * POST  /valoracions : Create a new valoracion.
     *
     * @param valoracion the valoracion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new valoracion, or with status 400 (Bad Request) if the valoracion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/valoracions")
    @Timed
    public ResponseEntity<Valoracion> createValoracion(@RequestBody Valoracion valoracion) throws URISyntaxException {
        log.debug("REST request to save Valoracion : {}", valoracion);
        if (valoracion.getId() != null) {
            throw new BadRequestAlertException("A new valoracion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Valoracion result = valoracionRepository.save(valoracion);
        return ResponseEntity.created(new URI("/api/valoracions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /valoracions : Updates an existing valoracion.
     *
     * @param valoracion the valoracion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated valoracion,
     * or with status 400 (Bad Request) if the valoracion is not valid,
     * or with status 500 (Internal Server Error) if the valoracion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/valoracions")
    @Timed
    public ResponseEntity<Valoracion> updateValoracion(@RequestBody Valoracion valoracion) throws URISyntaxException {
        log.debug("REST request to update Valoracion : {}", valoracion);
        if (valoracion.getId() == null) {
            return createValoracion(valoracion);
        }
        Valoracion result = valoracionRepository.save(valoracion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, valoracion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /valoracions : get all the valoracions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of valoracions in body
     */
    @GetMapping("/valoracions")
    @Timed
    public List<Valoracion> getAllValoracions() {
        log.debug("REST request to get all Valoracions");
        return valoracionRepository.findAll();
        }

    /**
     * GET  /valoracions/:id : get the "id" valoracion.
     *
     * @param id the id of the valoracion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the valoracion, or with status 404 (Not Found)
     */
    @GetMapping("/valoracions/{id}")
    @Timed
    public ResponseEntity<Valoracion> getValoracion(@PathVariable Long id) {
        log.debug("REST request to get Valoracion : {}", id);
        Valoracion valoracion = valoracionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(valoracion));
    }

    /**
     * DELETE  /valoracions/:id : delete the "id" valoracion.
     *
     * @param id the id of the valoracion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/valoracions/{id}")
    @Timed
    public ResponseEntity<Void> deleteValoracion(@PathVariable Long id) {
        log.debug("REST request to delete Valoracion : {}", id);
        valoracionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
