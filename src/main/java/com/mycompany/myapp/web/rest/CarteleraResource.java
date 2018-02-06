package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Cartelera;

import com.mycompany.myapp.repository.CarteleraRepository;
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
 * REST controller for managing Cartelera.
 */
@RestController
@RequestMapping("/api")
public class CarteleraResource {

    private final Logger log = LoggerFactory.getLogger(CarteleraResource.class);

    private static final String ENTITY_NAME = "cartelera";

    private final CarteleraRepository carteleraRepository;

    public CarteleraResource(CarteleraRepository carteleraRepository) {
        this.carteleraRepository = carteleraRepository;
    }

    /**
     * POST  /carteleras : Create a new cartelera.
     *
     * @param cartelera the cartelera to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cartelera, or with status 400 (Bad Request) if the cartelera has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/carteleras")
    @Timed
    public ResponseEntity<Cartelera> createCartelera(@RequestBody Cartelera cartelera) throws URISyntaxException {
        log.debug("REST request to save Cartelera : {}", cartelera);
        if (cartelera.getId() != null) {
            throw new BadRequestAlertException("A new cartelera cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cartelera result = carteleraRepository.save(cartelera);
        return ResponseEntity.created(new URI("/api/carteleras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /carteleras : Updates an existing cartelera.
     *
     * @param cartelera the cartelera to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cartelera,
     * or with status 400 (Bad Request) if the cartelera is not valid,
     * or with status 500 (Internal Server Error) if the cartelera couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/carteleras")
    @Timed
    public ResponseEntity<Cartelera> updateCartelera(@RequestBody Cartelera cartelera) throws URISyntaxException {
        log.debug("REST request to update Cartelera : {}", cartelera);
        if (cartelera.getId() == null) {
            return createCartelera(cartelera);
        }
        Cartelera result = carteleraRepository.save(cartelera);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cartelera.getId().toString()))
            .body(result);
    }

    /**
     * GET  /carteleras : get all the carteleras.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of carteleras in body
     */
    @GetMapping("/carteleras")
    @Timed
    public List<Cartelera> getAllCarteleras() {
        log.debug("REST request to get all Carteleras");
        return carteleraRepository.findAll();
        }

    /**
     * GET  /carteleras/:id : get the "id" cartelera.
     *
     * @param id the id of the cartelera to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cartelera, or with status 404 (Not Found)
     */
    @GetMapping("/carteleras/{id}")
    @Timed
    public ResponseEntity<Cartelera> getCartelera(@PathVariable Long id) {
        log.debug("REST request to get Cartelera : {}", id);
        Cartelera cartelera = carteleraRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cartelera));
    }

    /**
     * DELETE  /carteleras/:id : delete the "id" cartelera.
     *
     * @param id the id of the cartelera to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/carteleras/{id}")
    @Timed
    public ResponseEntity<Void> deleteCartelera(@PathVariable Long id) {
        log.debug("REST request to delete Cartelera : {}", id);
        carteleraRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
