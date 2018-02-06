package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ComentariosPeli.
 */
@Entity
@Table(name = "comentarios_peli")
public class ComentariosPeli implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "usuario")
    private Integer usuario;

    @Column(name = "peli")
    private Integer peli;

    @ManyToOne
    private Pelicula peliculaQueComenta;

    @ManyToOne
    private User usuarioQueComenta;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public ComentariosPeli comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public ComentariosPeli usuario(Integer usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public Integer getPeli() {
        return peli;
    }

    public ComentariosPeli peli(Integer peli) {
        this.peli = peli;
        return this;
    }

    public void setPeli(Integer peli) {
        this.peli = peli;
    }

    public Pelicula getPeliculaQueComenta() {
        return peliculaQueComenta;
    }

    public ComentariosPeli peliculaQueComenta(Pelicula pelicula) {
        this.peliculaQueComenta = pelicula;
        return this;
    }

    public void setPeliculaQueComenta(Pelicula pelicula) {
        this.peliculaQueComenta = pelicula;
    }

    public User getUsuarioQueComenta() {
        return usuarioQueComenta;
    }

    public ComentariosPeli usuarioQueComenta(User user) {
        this.usuarioQueComenta = user;
        return this;
    }

    public void setUsuarioQueComenta(User user) {
        this.usuarioQueComenta = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComentariosPeli comentariosPeli = (ComentariosPeli) o;
        if (comentariosPeli.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comentariosPeli.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComentariosPeli{" +
            "id=" + getId() +
            ", comentario='" + getComentario() + "'" +
            ", usuario=" + getUsuario() +
            ", peli=" + getPeli() +
            "}";
    }
}
