package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cartelera;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cartelera entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarteleraRepository extends JpaRepository<Cartelera, Long> {

}
