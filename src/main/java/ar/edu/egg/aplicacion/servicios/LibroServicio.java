
package ar.edu.egg.aplicacion.servicios;


import ar.edu.egg.aplicacion.entidades.Libro;
import ar.edu.egg.aplicacion.excepciones.WebException;
import ar.edu.egg.aplicacion.repositorios.AutorRepositorio;
import ar.edu.egg.aplicacion.repositorios.EditorialRepositorio;
import ar.edu.egg.aplicacion.repositorios.LibroRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LibroServicio {
 
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    @Autowired
    private LibroRepositorio libroRepositorio;
     
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Libro guardarLibro(Long isbn, String titulo, Integer anio, 
            Integer ejemplares, Integer ejemplaresPrestados,
            Integer ejemplaresRestantes, String autor, String editorial) throws Exception{
       
    
        validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados,ejemplaresRestantes, autor, editorial);
        Libro libro = new Libro();
        
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplaresRestantes);
        libro.setAlta(true);
        libro.setAutor(autorRepositorio.getById(autor));
        libro.setEditorial(editorialRepositorio.getById(editorial));
        return libroRepositorio.save(libro);
    
    }
    
    @Transactional(readOnly = true)
	public List<Libro> listarActivos() {
		return libroRepositorio.findByAltaTrueOrderByAlta();
	}

	@Transactional(readOnly = true)
	public List<Libro> listarTodos() {
		return libroRepositorio.findAllByOrderByTituloAsc();
	}
        
        @Transactional(readOnly = true)
	public List<Libro> buscarPorTitulo(String titulo) {
		return libroRepositorio.findByTituloContainingOrderByTitulo(titulo);
	}
        
        @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Libro alta(String id) {

		Libro entidad = libroRepositorio.getOne(id);

		entidad.setAlta(true);
		return libroRepositorio.save(entidad);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Libro baja(String id) {

		Libro entidad = libroRepositorio.getOne(id);

		entidad.setAlta(false);
		return libroRepositorio.save(entidad);
	}
        
       public void validar(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, String autor, String editorial) throws WebException {

        if (titulo == null || titulo.isEmpty() || titulo.contains("  ")) {
            throw new WebException("Titulo invalido");
        }

        if (autor == null || autor.isEmpty() || autor.contains("  ")) {
            throw new WebException("Autor invalido");
        }

        if (editorial == null || editorial.isEmpty() || editorial.contains("  ")) {
            throw new WebException("Editorial invalido");
        }

        if (isbn == null || anio == null || ejemplares == null || ejemplaresPrestados == null || ejemplaresRestantes == null) {
            throw new WebException("Datos invalido");
        }
    }
}
