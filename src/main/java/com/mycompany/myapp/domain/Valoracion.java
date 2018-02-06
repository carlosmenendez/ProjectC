package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Valoracion.
 */
@Entity
@Table(name = "valoracion")
public class Valoracion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nota")
    private Double nota;

    @Column(name = "usuario_votado")
    private Integer usuarioVotado;

    @Column(name = "pelicula_votada")
    private Integer peliculaVotada;

    @ManyToOne
    private User userVote;

    @ManyToOne
    private Pelicula userVoteMovie;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNota() {
        return nota;
    }

    public Valoracion nota(Double nota) {
        this.nota = nota;
        return this;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Integer getUsuarioVotado() {
        return usuarioVotado;
    }

    public Valoracion usuarioVotado(Integer usuarioVotado) {
        this.usuarioVotado = usuarioVotado;
        return this;
    }

    public void setUsuarioVotado(Integer usuarioVotado) {
        this.usuarioVotado = usuarioVotado;
    }

    public Integer getPeliculaVotada() {
        return peliculaVotada;
    }

    public Valoracion peliculaVotada(Integer peliculaVotada) {
        this.peliculaVotada = peliculaVotada;
        return this;
    }

    public void setPeliculaVotada(Integer peliculaVotada) {
        this.peliculaVotada = peliculaVotada;
    }

    public User getUserVote() {
        return userVote;
    }

    public Valoracion userVote(User user) {
        this.userVote = user;
        return this;
    }

    public void setUserVote(User user) {
        this.userVote = user;
    }

    public Pelicula getUserVoteMovie() {
        return userVoteMovie;
    }

    public Valoracion userVoteMovie(Pelicula pelicula) {
        this.userVoteMovie = pelicula;
        return this;
    }

    public void setUserVoteMovie(Pelicula pelicula) {
        this.userVoteMovie = pelicula;
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
        Valoracion valoracion = (Valoracion) o;
        if (valoracion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), valoracion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Valoracion{" +
            "id=" + getId() +
            ", nota=" + getNota() +
            ", usuarioVotado=" + getUsuarioVotado() +
            ", peliculaVotada=" + getPeliculaVotada() +
            "}";
    }
}
