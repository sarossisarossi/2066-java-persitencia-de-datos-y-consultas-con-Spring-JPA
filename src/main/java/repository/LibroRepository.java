package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Long> {
    boolean existsById(Long id);
    @Query("SELECT DISTINCT l.nombreAutor FROM Libro l WHERE l.nombreAutor IS NOT NULL ORDER BY l.nombreAutor ASC")
    List<String> findDistinctAutoresOrdered();

    @Query("SELECT DISTINCT l.nombreAutor FROM Libro l WHERE l.muerteAutor > :an AND l.nacimientoAutor < :an")
    List<String> findAutoresVivos(@Param("an") int an);

    @Query("SELECT DISTINCT l.titulo FROM Libro l WHERE l.listadoIdiomas LIKE CONCAT('%', :idioma, '%')")
    List<String> findLibrosIdioma(@Param("idioma") String idioma);


}