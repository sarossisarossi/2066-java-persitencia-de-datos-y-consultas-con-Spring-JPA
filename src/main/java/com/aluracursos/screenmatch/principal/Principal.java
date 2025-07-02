package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosLibro;
import com.aluracursos.screenmatch.model.Libro;
import com.aluracursos.screenmatch.model.DatosResponse;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import com.aluracursos.screenmatch.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Principal implements CommandLineRunner {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String URL_BASE_GUT = "https://gutendex.com/books?search=";
    private final String API_KEY = "&apikey=TU-APIKEY-OMDB";
    private final LibroRepository libroRepository;

    @Autowired
    public Principal(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        muestraElMenu();
    }

    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por título 
                    2 - Listado de libros registrados en la Base de Datos
                    3 - Listado de autores registrados
                    4 - Listado de autores vivos en un año especifico
                    5 - Listado de libros por idioma(ES ,EN ,FR ,PT)
                                  
                    0 - Salir
                    """;
            try {
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch(InputMismatchException e){
                System.out.println("Entrada inválida. Debes ingresar un número.");
                teclado.nextLine();
                muestraElMenu();

            }
            switch (opcion) {
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibroIdioma();
                    break;


                case 0:
                    System.out.println("Cerrando la aplicación...");
                    System.exit(0);
                default:
                    System.out.println("Opción inválida");
                    muestraElMenu();
            }
        }

    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private DatosLibro getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        var url = URL_BASE_GUT + nombreLibro.replace(" ", "%20").replace("'", "%27");
        System.out.println(url);
        var json = consumoApi.obtenerDatos(url);
        System.out.println(json);
        int contador = 0;
        DatosResponse datos = conversor.obtenerDatos(json, DatosResponse.class);
        // Recorremos todos los libros de la lista buscando el primero que NO esté en la BD
        for (DatosLibro libro : datos.listadoLibros()) {
            if (libroRepository.existsById((long) libro.id())) {
                System.out.println( "Este Libro esta en la base de datos");

                 // este libro no está en la BD → lo retornamos
            }
            if (!libroRepository.existsById((long) libro.id())) {
                System.out.println( "Este Libro NO NO NO esta en la base de datos");
                return libro;
                // este libro no está en la BD → lo retornamos
            }
        }

        return datos.listadoLibros().get(0);
    }

    private void listarLibros() {
        List<Libro> libros = libroRepository.findAll(Sort.by(Sort.Direction.ASC, "titulo"));

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Libros registrados:");
            for (Libro libro : libros) {
                System.out.println("- " + libro.getTitulo());
            }
        }
    }

    private void listarAutores() {
        List<Libro> libros = libroRepository.findAll(Sort.by(Sort.Direction.ASC, "nombreAutor"));

        if (libros.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("Autores registrados:");
            List<String> autores = libroRepository.findDistinctAutoresOrdered();
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivos() {


        List<Libro> libros = libroRepository.findAll(Sort.by(Sort.Direction.ASC, "nombreAutor"));
        int an = 1900;
        if (libros.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            try {
                System.out.println("escriba un año");
                an = teclado.nextInt();
                teclado.nextLine();
            }
            catch(InputMismatchException e){
                System.out.println("Entrada inválida. Debes ingresar un número entero.");
                teclado.nextLine();
                muestraElMenu();

            }
            List<String> autores = libroRepository.findAutoresVivos(an);

            if (!autores.isEmpty()) {
                System.out.println("Autores vivos en el año " + an + ":");
                autores.forEach(System.out::println);
            } else {
                System.out.println("No hay autores vivos en el año: " + an);
            }
            System.out.println();

        }
    }

    private void listarLibroIdioma() {


        List<Libro> libros = libroRepository.findAll(Sort.by(Sort.Direction.ASC, "titulo"));
        String idioma = "NN";
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            try {
                System.out.println("escriba un idioma");
                idioma = teclado.next();
                idioma = idioma.toUpperCase();
                teclado.nextLine();
            }
            catch(InputMismatchException e){
                System.out.println("Entrada inválida. Debes ingresar un número entero.");
                teclado.nextLine();
                muestraElMenu();

            }
            List<String> librosidioma = libroRepository.findLibrosIdioma(idioma);

            if (!librosidioma.isEmpty()) {
                System.out.println("libros con el idioma " + idioma + ":");
                librosidioma.forEach(System.out::println);
            } else {
                System.out.println("No hay libros del idioma: " + idioma);
            }
            System.out.println();

        }
    }

    private void buscarEpisodioPorSerie() {
        DatosSerie datosSerie = getDatosSerie();
        List<DatosTemporadas> temporadas = new ArrayList<>();

        for (int i = 1; i <= datosSerie.totalTemporadas(); i++) {
            var json = consumoApi.obtenerDatos(URL_BASE + datosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporada);
        }
        temporadas.forEach(System.out::println);
    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        System.out.println(datos);
    }

    private void buscarLibroWeb(){
        try {
            DatosLibro datos = getDatosLibro();
            if (datos == null) {
                System.out.println("No se encontró un libro válido.");
                muestraElMenu();
                return;
            }

            System.out.println(datos);
            var libro = new Libro(datos);
            libroRepository.save(libro);
        } catch (Exception e) {
            System.out.println("Ocurrió un error al buscar el libro: " + e.getMessage());
            muestraElMenu(); // vuelve al menú principal
        }
    }


}

