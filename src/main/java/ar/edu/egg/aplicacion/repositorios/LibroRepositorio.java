package ar.edu.egg.aplicacion.repositorios;

import ar.edu.egg.aplicacion.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LibroRepositorio extends JpaRepository<Libro,String> {

    
    public List<Libro> findByAltaTrueOrderByAlta();
    
    public List<Libro> findAllByOrderByTituloAsc();
    
    public List<Libro> findByTituloContainingOrderByTitulo(String nombre);
    
    

}
