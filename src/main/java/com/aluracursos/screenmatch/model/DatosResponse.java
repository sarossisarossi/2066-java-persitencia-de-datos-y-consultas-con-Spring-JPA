package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosResponse(
        @JsonAlias("count") Integer conteoDescargas,
        @JsonAlias("results") List<Book> listadoLibros
) {}
