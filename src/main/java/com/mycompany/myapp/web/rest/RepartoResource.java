package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Reparto;

import com.mycompany.myapp.repository.RepartoRepository;
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
 * REST controller for managing Reparto.
 */
@RestController
@RequestMapping("/api")
public class RepartoResource {

    private final Logger log = LoggerFactory.getLogger(RepartoResource.class);

    private static final String ENTITY_NAME = "reparto";

    private final RepartoRepository repartoRepository;

    public RepartoResource(RepartoRepository repartoRepository) {
        this.repartoRepository = repartoRepository;
    }

    /**
     * POST  /repartos : Create a new reparto.
     *
     * @param reparto the reparto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reparto, or with status 400 (Bad Request) if the reparto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/repartos")
    @Timed
    public ResponseEntity<Reparto> createReparto(@RequestBody Reparto reparto) throws URISyntaxException {
        log.debug("REST request to save Reparto : {}", reparto);
        if (reparto.getId() != null) {
            throw new BadRequestAlertException("A new reparto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reparto result = repartoRepository.save(reparto);
        return ResponseEntity.created(new URI("/api/repartos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /repartos : Updates an existing reparto.
     *
     * @param reparto the reparto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reparto,
     * or with status 400 (Bad Request) if the reparto is not valid,
     * or with status 500 (Internal Server Error) if the reparto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/repartos")
    @Timed
    public ResponseEntity<Reparto> updateReparto(@RequestBody Reparto reparto) throws URISyntaxException {
        log.debug("REST request to update Reparto : {}", reparto);
        if (reparto.getId() == null) {
            return createReparto(reparto);
        }
        Reparto result = repartoRepository.save(reparto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reparto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /repartos : get all the repartos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of repartos in body
     */
    @GetMapping("/repartos")
    @Timed
    public List<Reparto> getAllRepartos() {
        log.debug("REST request to get all Repartos");
        return repartoRepository.findAll();
        }

    /**
     * GET  /repartos/:id : get the "id" reparto.
     *
     * @param id the id of the reparto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reparto, or with status 404 (Not Found)
     */
    @GetMapping("/repartos/{id}")
    @Timed
    public ResponseEntity<Reparto> getReparto(@PathVariable Long id) {
        log.debug("REST request to get Reparto : {}", id);
        Reparto reparto = repartoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reparto));
    }

    /**
     * DELETE  /repartos/:id : delete the "id" reparto.
     *
     * @param id the id of the reparto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/repartos/{id}")
    @Timed
    public ResponseEntity<Void> deleteReparto(@PathVariable Long id) {
        log.debug("REST request to delete Reparto : {}", id);
        repartoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
