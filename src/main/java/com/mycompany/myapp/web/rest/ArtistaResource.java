package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Artista;

import com.mycompany.myapp.repository.ArtistaRepository;
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
 * REST controller for managing Artista.
 */
@RestController
@RequestMapping("/api")
public class ArtistaResource {

    private final Logger log = LoggerFactory.getLogger(ArtistaResource.class);

    private static final String ENTITY_NAME = "artista";

    private final ArtistaRepository artistaRepository;

    public ArtistaResource(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    /**
     * POST  /artistas : Create a new artista.
     *
     * @param artista the artista to create
     * @return the ResponseEntity with status 201 (Created) and with body the new artista, or with status 400 (Bad Request) if the artista has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/artistas")
    @Timed
    public ResponseEntity<Artista> createArtista(@RequestBody Artista artista) throws URISyntaxException {
        log.debug("REST request to save Artista : {}", artista);
        if (artista.getId() != null) {
            throw new BadRequestAlertException("A new artista cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Artista result = artistaRepository.save(artista);
        return ResponseEntity.created(new URI("/api/artistas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /artistas : Updates an existing artista.
     *
     * @param artista the artista to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated artista,
     * or with status 400 (Bad Request) if the artista is not valid,
     * or with status 500 (Internal Server Error) if the artista couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/artistas")
    @Timed
    public ResponseEntity<Artista> updateArtista(@RequestBody Artista artista) throws URISyntaxException {
        log.debug("REST request to update Artista : {}", artista);
        if (artista.getId() == null) {
            return createArtista(artista);
        }
        Artista result = artistaRepository.save(artista);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, artista.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artistas : get all the artistas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of artistas in body
     */
    @GetMapping("/artistas")
    @Timed
    public List<Artista> getAllArtistas() {
        log.debug("REST request to get all Artistas");
        return artistaRepository.findAll();
        }

    /**
     * GET  /artistas/:id : get the "id" artista.
     *
     * @param id the id of the artista to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the artista, or with status 404 (Not Found)
     */
    @GetMapping("/artistas/{id}")
    @Timed
    public ResponseEntity<Artista> getArtista(@PathVariable Long id) {
        log.debug("REST request to get Artista : {}", id);
        Artista artista = artistaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(artista));
    }

    /**
     * DELETE  /artistas/:id : delete the "id" artista.
     *
     * @param id the id of the artista to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/artistas/{id}")
    @Timed
    public ResponseEntity<Void> deleteArtista(@PathVariable Long id) {
        log.debug("REST request to delete Artista : {}", id);
        artistaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
