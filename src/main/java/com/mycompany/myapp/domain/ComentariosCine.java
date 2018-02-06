package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ComentariosCine.
 */
@Entity
@Table(name = "comentarios_cine")
public class ComentariosCine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "usuario")
    private Integer usuario;

    @Column(name = "cine")
    private Integer cine;

    @ManyToOne
    private Cine cineQueComenta;

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

    public ComentariosCine comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public ComentariosCine usuario(Integer usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public Integer getCine() {
        return cine;
    }

    public ComentariosCine cine(Integer cine) {
        this.cine = cine;
        return this;
    }

    public void setCine(Integer cine) {
        this.cine = cine;
    }

    public Cine getCineQueComenta() {
        return cineQueComenta;
    }

    public ComentariosCine cineQueComenta(Cine cine) {
        this.cineQueComenta = cine;
        return this;
    }

    public void setCineQueComenta(Cine cine) {
        this.cineQueComenta = cine;
    }

    public User getUsuarioQueComenta() {
        return usuarioQueComenta;
    }

    public ComentariosCine usuarioQueComenta(User user) {
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
        ComentariosCine comentariosCine = (ComentariosCine) o;
        if (comentariosCine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comentariosCine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComentariosCine{" +
            "id=" + getId() +
            ", comentario='" + getComentario() + "'" +
            ", usuario=" + getUsuario() +
            ", cine=" + getCine() +
            "}";
    }
}
