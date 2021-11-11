
package ar.edu.egg.aplicacion.controladores;

import ar.edu.egg.aplicacion.excepciones.WebException;
import ar.edu.egg.aplicacion.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioServicio usuarioServicio;


	@GetMapping("/registro")
	public String formulario() {
		return "login-crear";
	}

	@PostMapping("/registro")
	public String guardar(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido,
			@RequestParam String correo, @RequestParam String clave, @RequestParam String rol) {

		try {
			usuarioServicio.guardar(nombre, apellido, correo, clave, rol);

			modelo.put("exito", "registro exitoso");
			return "login-crear";
		} catch (WebException e) {
			modelo.put("error", e.getMessage());
			return "login-crear";
		}
	}
}

