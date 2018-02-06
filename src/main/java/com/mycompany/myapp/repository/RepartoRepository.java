package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Reparto;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Reparto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepartoRepository extends JpaRepository<Reparto, Long> {

}
