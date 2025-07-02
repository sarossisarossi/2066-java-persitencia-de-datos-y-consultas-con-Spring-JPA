package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("id") long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> listadoAutores,
        @JsonAlias("languages") List<String> listadoIdiomas
        // Puedes agregar más campos aquí si quieres
){}