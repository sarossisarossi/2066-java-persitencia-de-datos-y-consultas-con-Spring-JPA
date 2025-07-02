package com.aluracursos.screenmatch.model;

import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.Stream;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    private Long id;

    @Column(unique = true)
    private String titulo;

    private String nombreAutor;

    private Integer nacimientoAutor;

    private Integer muerteAutor;

    @Column(name = "listado_idiomas")
    private String listadoIdiomas;

    // Constructor vacío obligatorio para JPA
    public Libro() {
    }

    // Constructor con DatosLibro
    public Libro(DatosLibro datosLibro) {
        this.id = datosLibro.id();
        this.titulo = datosLibro.titulo();
        if (datosLibro.listadoAutores() != null && !datosLibro.listadoAutores().isEmpty()) {
            this.nombreAutor = datosLibro.listadoAutores().get(0).nombreAutor();
            this.nacimientoAutor = datosLibro.listadoAutores().get(0).nacimientoAutor();
            this.muerteAutor = datosLibro.listadoAutores().get(0).muerteAutor();
        } else {
            this.nombreAutor = null;
            this.nacimientoAutor = null;
            this.muerteAutor = null;
        }

        this.listadoIdiomas =  datosLibro.listadoIdiomas().stream()
                .map(String::toUpperCase)
                .collect(Collectors.joining(","));



    }

    // Getters y setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getNombreAutor() {
        return nombreAutor;
    }
    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }
    public Integer getNacimientoAutor() {
        return nacimientoAutor;
    }
    public void setNacimientoAutor(Integer nacimientoAutor) {
        this.nacimientoAutor = nacimientoAutor;
    }
    public Integer getMuerteAutor() {
        return muerteAutor;
    }
    public void setMuerteAutor(Integer muerteAutor) {
        this.muerteAutor = muerteAutor;
    }
    public String getListadoIdiomas() {
        return listadoIdiomas;
    }
    public void setListadoIdiomas(String listadoIdiomas) {
        this.listadoIdiomas = listadoIdiomas;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", nombreAutor='" + nombreAutor + '\'' +
                ", nacimientoAutor=" + nacimientoAutor +
                ", muerteAutor=" + muerteAutor +
                ", listadoIdiomas='" + listadoIdiomas + '\'' +
                '}';
    }
}
