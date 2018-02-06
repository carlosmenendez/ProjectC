package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Salas;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Salas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalasRepository extends JpaRepository<Salas, Long> {

}
