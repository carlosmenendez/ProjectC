package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ComentariosPeli;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the ComentariosPeli entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComentariosPeliRepository extends JpaRepository<ComentariosPeli, Long> {

    @Query("select comentarios_peli from ComentariosPeli comentarios_peli where comentarios_peli.usuarioQueComenta.login = ?#{principal.username}")
    List<ComentariosPeli> findByUsuarioQueComentaIsCurrentUser();

}
