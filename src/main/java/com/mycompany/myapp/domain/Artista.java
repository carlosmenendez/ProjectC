package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Artista.
 */
@Entity
@Table(name = "artista")
public class Artista implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Column(name = "fecha_nacimiento")
    private ZonedDateTime fechaNacimiento;

    @Column(name = "es_director")
    private Boolean esDirector;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public Artista nombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        return this;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public ZonedDateTime getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Artista fechaNacimiento(ZonedDateTime fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public void setFechaNacimiento(ZonedDateTime fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Boolean isEsDirector() {
        return esDirector;
    }

    public Artista esDirector(Boolean esDirector) {
        this.esDirector = esDirector;
        return this;
    }

    public void setEsDirector(Boolean esDirector) {
        this.esDirector = esDirector;
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
        Artista artista = (Artista) o;
        if (artista.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), artista.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Artista{" +
            "id=" + getId() +
            ", nombreCompleto='" + getNombreCompleto() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", esDirector='" + isEsDirector() + "'" +
            "}";
    }
}
