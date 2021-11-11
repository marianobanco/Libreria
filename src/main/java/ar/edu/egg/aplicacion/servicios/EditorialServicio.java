
package ar.edu.egg.aplicacion.servicios;




import ar.edu.egg.aplicacion.entidades.Editorial;
import ar.edu.egg.aplicacion.repositorios.EditorialRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {
     @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Editorial guardar(String nombre){
        
        Editorial editorial = new Editorial();
        
        editorial.setNombre(nombre);
        editorial.setAlta(true);
        
        return editorialRepositorio.save(editorial);
    }
    
    @Transactional(readOnly = true)
	public List<Editorial> buscarPorNombre(String nombre) {
		return editorialRepositorio.findByNombreContainingOrderByNombre(nombre);
	}
        
        @Transactional(readOnly = true)
	public List<Editorial> listarTodos() {
		return editorialRepositorio.findAllByOrderByNombreAsc();
	}
        
        @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Editorial alta(String id) {

		Editorial entidad = editorialRepositorio.getOne(id);

		entidad.setAlta(true);
		return editorialRepositorio.save(entidad);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Editorial baja(String id) {

		Editorial entidad = editorialRepositorio.getOne(id);

		entidad.setAlta(false);
		return editorialRepositorio.save(entidad);
	}
}
