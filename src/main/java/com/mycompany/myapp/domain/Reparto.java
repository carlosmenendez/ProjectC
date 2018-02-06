package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Reparto.
 */
@Entity
@Table(name = "reparto")
public class Reparto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_artista")
    private Integer nombreArtista;

    @Column(name = "rol")
    private Integer rol;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "pelicula")
    private Integer pelicula;

    @ManyToOne
    private Artista artistaEnReparto;

    @ManyToOne
    private Pelicula peliculaDelReparto;

    @ManyToOne
    private Roles rolDelArtista;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNombreArtista() {
        return nombreArtista;
    }

    public Reparto nombreArtista(Integer nombreArtista) {
        this.nombreArtista = nombreArtista;
        return this;
    }

    public void setNombreArtista(Integer nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    public Integer getRol() {
        return rol;
    }

    public Reparto rol(Integer rol) {
        this.rol = rol;
        return this;
    }

    public void setRol(Integer rol) {
        this.rol = rol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Reparto descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPelicula() {
        return pelicula;
    }

    public Reparto pelicula(Integer pelicula) {
        this.pelicula = pelicula;
        return this;
    }

    public void setPelicula(Integer pelicula) {
        this.pelicula = pelicula;
    }

    public Artista getArtistaEnReparto() {
        return artistaEnReparto;
    }

    public Reparto artistaEnReparto(Artista artista) {
        this.artistaEnReparto = artista;
        return this;
    }

    public void setArtistaEnReparto(Artista artista) {
        this.artistaEnReparto = artista;
    }

    public Pelicula getPeliculaDelReparto() {
        return peliculaDelReparto;
    }

    public Reparto peliculaDelReparto(Pelicula pelicula) {
        this.peliculaDelReparto = pelicula;
        return this;
    }

    public void setPeliculaDelReparto(Pelicula pelicula) {
        this.peliculaDelReparto = pelicula;
    }

    public Roles getRolDelArtista() {
        return rolDelArtista;
    }

    public Reparto rolDelArtista(Roles roles) {
        this.rolDelArtista = roles;
        return this;
    }

    public void setRolDelArtista(Roles roles) {
        this.rolDelArtista = roles;
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
        Reparto reparto = (Reparto) o;
        if (reparto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reparto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reparto{" +
            "id=" + getId() +
            ", nombreArtista=" + getNombreArtista() +
            ", rol=" + getRol() +
            ", descripcion='" + getDescripcion() + "'" +
            ", pelicula=" + getPelicula() +
            "}";
    }
}
