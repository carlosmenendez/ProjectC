package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Cartelera.
 */
@Entity
@Table(name = "cartelera")
public class Cartelera implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dia")
    private ZonedDateTime dia;

    @Column(name = "cine")
    private Integer cine;

    @Column(name = "pelicula")
    private Integer pelicula;

    @Column(name = "sala")
    private Integer sala;

    @ManyToOne
    private Pelicula peliDeCartelera;

    @ManyToOne
    private Cine cineDeCartelera;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDia() {
        return dia;
    }

    public Cartelera dia(ZonedDateTime dia) {
        this.dia = dia;
        return this;
    }

    public void setDia(ZonedDateTime dia) {
        this.dia = dia;
    }

    public Integer getCine() {
        return cine;
    }

    public Cartelera cine(Integer cine) {
        this.cine = cine;
        return this;
    }

    public void setCine(Integer cine) {
        this.cine = cine;
    }

    public Integer getPelicula() {
        return pelicula;
    }

    public Cartelera pelicula(Integer pelicula) {
        this.pelicula = pelicula;
        return this;
    }

    public void setPelicula(Integer pelicula) {
        this.pelicula = pelicula;
    }

    public Integer getSala() {
        return sala;
    }

    public Cartelera sala(Integer sala) {
        this.sala = sala;
        return this;
    }

    public void setSala(Integer sala) {
        this.sala = sala;
    }

    public Pelicula getPeliDeCartelera() {
        return peliDeCartelera;
    }

    public Cartelera peliDeCartelera(Pelicula pelicula) {
        this.peliDeCartelera = pelicula;
        return this;
    }

    public void setPeliDeCartelera(Pelicula pelicula) {
        this.peliDeCartelera = pelicula;
    }

    public Cine getCineDeCartelera() {
        return cineDeCartelera;
    }

    public Cartelera cineDeCartelera(Cine cine) {
        this.cineDeCartelera = cine;
        return this;
    }

    public void setCineDeCartelera(Cine cine) {
        this.cineDeCartelera = cine;
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
        Cartelera cartelera = (Cartelera) o;
        if (cartelera.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cartelera.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cartelera{" +
            "id=" + getId() +
            ", dia='" + getDia() + "'" +
            ", cine=" + getCine() +
            ", pelicula=" + getPelicula() +
            ", sala=" + getSala() +
            "}";
    }
}
