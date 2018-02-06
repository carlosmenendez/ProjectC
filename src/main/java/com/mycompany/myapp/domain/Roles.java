package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Roles.
 */
@Entity
@Table(name = "roles")
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_rol")
    private String nombreRol;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public Roles nombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
        return this;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
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
        Roles roles = (Roles) o;
        if (roles.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roles.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Roles{" +
            "id=" + getId() +
            ", nombreRol='" + getNombreRol() + "'" +
            "}";
    }
}
