package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ComentariosCine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the ComentariosCine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComentariosCineRepository extends JpaRepository<ComentariosCine, Long> {

    @Query("select comentarios_cine from ComentariosCine comentarios_cine where comentarios_cine.usuarioQueComenta.login = ?#{principal.username}")
    List<ComentariosCine> findByUsuarioQueComentaIsCurrentUser();

}
