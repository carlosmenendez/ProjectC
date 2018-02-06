package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Artista;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Artista entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {

}
