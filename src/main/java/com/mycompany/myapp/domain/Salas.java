package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Salas.
 */
@Entity
@Table(name = "salas")
public class Salas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "denominacion")
    private Integer denominacion;

    @Column(name = "aforo")
    private Integer aforo;

    @Column(name = "cine")
    private Integer cine;

    @ManyToOne
    private Cine cineDeLaSala;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDenominacion() {
        return denominacion;
    }

    public Salas denominacion(Integer denominacion) {
        this.denominacion = denominacion;
        return this;
    }

    public void setDenominacion(Integer denominacion) {
        this.denominacion = denominacion;
    }

    public Integer getAforo() {
        return aforo;
    }

    public Salas aforo(Integer aforo) {
        this.aforo = aforo;
        return this;
    }

    public void setAforo(Integer aforo) {
        this.aforo = aforo;
    }

    public Integer getCine() {
        return cine;
    }

    public Salas cine(Integer cine) {
        this.cine = cine;
        return this;
    }

    public void setCine(Integer cine) {
        this.cine = cine;
    }

    public Cine getCineDeLaSala() {
        return cineDeLaSala;
    }

    public Salas cineDeLaSala(Cine cine) {
        this.cineDeLaSala = cine;
        return this;
    }

    public void setCineDeLaSala(Cine cine) {
        this.cineDeLaSala = cine;
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
        Salas salas = (Salas) o;
        if (salas.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salas.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Salas{" +
            "id=" + getId() +
            ", denominacion=" + getDenominacion() +
            ", aforo=" + getAforo() +
            ", cine=" + getCine() +
            "}";
    }
}
