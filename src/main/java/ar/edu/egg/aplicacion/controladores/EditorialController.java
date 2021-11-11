/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.egg.aplicacion.controladores;



import ar.edu.egg.aplicacion.entidades.Editorial;
import ar.edu.egg.aplicacion.servicios.EditorialServicio;
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
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/cargareditorial")
    public String formulario() {
        return "cargar-editorial";
    }
    
     @PostMapping("/cargareditorial")
    public String lista(ModelMap modelo, @RequestParam String nombre) {

        List<Editorial> editoriales = editorialServicio.buscarPorNombre(nombre);

        modelo.addAttribute("editoriales", editoriales);

        modelo.put("buscareditorial", "Registro exitoso");
        return "cargar-editorial";
    }
    
        @PostMapping("/registro")
    public String guardarAutor(ModelMap modelo, @RequestParam String nombre) {

        try {

            editorialServicio.guardar(nombre);

            modelo.put("exito", "Registro exitoso");
            return "cargar-editorial";
        } catch (Exception e) {
            modelo.put("error", "Falto algun dato");
            return "cargar-editorial";
        }
    }
    
    @GetMapping("/baja/{id}")
	public String baja(ModelMap modelo, @PathVariable String id) {

		try {
			editorialServicio.baja(id);
			return "redirect:/editorial/cargareditorial";
		} catch (Exception e) {
			return "redirect:/";
		}
	}
	@GetMapping("/alta/{id}")
	public String alta(ModelMap modelo, @PathVariable String id) {

		try {
			editorialServicio.alta(id);
			return "redirect:/editorial/cargareditorial";
		} catch (Exception e) {
			return "redirect:/";
		}
	}
}
