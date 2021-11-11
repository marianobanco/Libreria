package ar.edu.egg.aplicacion.controladores;

import ar.edu.egg.aplicacion.entidades.Autor;
import ar.edu.egg.aplicacion.entidades.Editorial;
import ar.edu.egg.aplicacion.entidades.Libro;
import ar.edu.egg.aplicacion.servicios.AutorServicio;
import ar.edu.egg.aplicacion.servicios.EditorialServicio;
import ar.edu.egg.aplicacion.servicios.LibroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registro")
    public String formulario(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarTodos();
        modelo.addAttribute("autores", autores);
        List<Editorial> editoriales = editorialServicio.listarTodos();
        modelo.addAttribute("editoriales", editoriales);

        return "cargar-libro";
    }

    @PostMapping("/registro")
    public String guardarLibro(ModelMap modelo, @RequestParam Long isbn,
            @RequestParam String titulo, @RequestParam Integer anio,
            @RequestParam Integer ejemplares,
            @RequestParam Integer ejemplaresPrestados,
            @RequestParam Integer ejemplaresRestantes,
            @RequestParam String autor, @RequestParam String editorial) {

        try {

            libroServicio.guardarLibro(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);

            modelo.put("exito", "Registro exitoso");
            return "cargar-libro";
        } catch (Exception e) {
            modelo.put("error", "Falto algun dato");
            return "cargar-libro";
        }
    }

    @PostMapping("/lista")
    public String lista(ModelMap modelo, @RequestParam String nombre) {

        List<Libro> libros = libroServicio.buscarPorTitulo(nombre);

        modelo.addAttribute("libros", libros);

        modelo.put("buscarlibro", "Registro exitoso");
        return "cargar-libro";
    }

    @GetMapping("/baja/{id}")
    public String baja(ModelMap modelo, @PathVariable String id) {

        try {
            libroServicio.baja(id);
            return "redirect:/libro/lista";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) {

        try {
            libroServicio.alta(id);
            return "redirect:/libro/lista";
        } catch (Exception e) {
            return "redirect:/";
        }
    }
}
