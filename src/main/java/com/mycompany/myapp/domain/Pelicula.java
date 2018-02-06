package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Pelicula.
 */
@Entity
@Table(name = "pelicula")
public class Pelicula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "pais")
    private String pais;

    @Column(name = "duracion")
    private Integer duracion;

    @Column(name = "fecha_de_estreno")
    private ZonedDateTime fechaDeEstreno;

    @Column(name = "genero")
    private String genero;

    @Column(name = "es_estreno")
    private Boolean esEstreno;

    @Column(name = "sinopsis")
    private String sinopsis;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Pelicula nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public Pelicula pais(String pais) {
        this.pais = pais;
        return this;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public Pelicula duracion(Integer duracion) {
        this.duracion = duracion;
        return this;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public ZonedDateTime getFechaDeEstreno() {
        return fechaDeEstreno;
    }

    public Pelicula fechaDeEstreno(ZonedDateTime fechaDeEstreno) {
        this.fechaDeEstreno = fechaDeEstreno;
        return this;
    }

    public void setFechaDeEstreno(ZonedDateTime fechaDeEstreno) {
        this.fechaDeEstreno = fechaDeEstreno;
    }

    public String getGenero() {
        return genero;
    }

    public Pelicula genero(String genero) {
        this.genero = genero;
        return this;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Boolean isEsEstreno() {
        return esEstreno;
    }

    public Pelicula esEstreno(Boolean esEstreno) {
        this.esEstreno = esEstreno;
        return this;
    }

    public void setEsEstreno(Boolean esEstreno) {
        this.esEstreno = esEstreno;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public Pelicula sinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
        return this;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
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
        Pelicula pelicula = (Pelicula) o;
        if (pelicula.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pelicula.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pelicula{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", pais='" + getPais() + "'" +
            ", duracion=" + getDuracion() +
            ", fechaDeEstreno='" + getFechaDeEstreno() + "'" +
            ", genero='" + getGenero() + "'" +
            ", esEstreno='" + isEsEstreno() + "'" +
            ", sinopsis='" + getSinopsis() + "'" +
            "}";
    }
}
