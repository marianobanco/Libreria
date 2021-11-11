package ar.edu.egg.aplicacion.servicios;

import ar.edu.egg.aplicacion.entidades.Usuario;
import ar.edu.egg.aplicacion.enumeracion.Rol;
import ar.edu.egg.aplicacion.excepciones.WebException;
import ar.edu.egg.aplicacion.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {WebException.class, Exception.class})
    public Usuario guardar(String nombre, String apellido, String correo,
            String clave, String rol) throws WebException {

        validar(nombre, apellido, correo, clave, rol);

        Usuario entidad = new Usuario();

        entidad.setNombre(nombre);
        entidad.setApellido(apellido);
        entidad.setCorreo(correo);
        entidad.setClave(new BCryptPasswordEncoder().encode(clave));
        entidad.setRol(Rol.valueOf(rol));
        entidad.setAlta(true);

        return usuarioRepositorio.save(entidad);
    }

    public void validar(String nombre, String apellido, String correo, 
            String clave, String rol) throws WebException {

        if (nombre == null || nombre.isEmpty() || nombre.contains("  ")) {
            throw new WebException("Debe tener un nombre valido");
        }

        if (apellido == null || apellido.isEmpty() || apellido.contains("  ")) {
            throw new WebException("Debe tener un apellido valido");
        }

        if (correo == null || correo.isEmpty() || correo.contains("  ")) {
            throw new WebException("Debe tener un email valido");
        }

        if (usuarioRepositorio.buscarPorEmail(correo) != null) {
            throw new WebException("El Email ya esta en uso");
        }

        if (clave == null || clave.isEmpty() || clave.contains("  ") || clave.length() < 8 || clave.length() > 12) {
            throw new WebException("Debe tener una clave valida");
        }

        if (!Rol.ADMIN.toString().equals(rol) && !Rol.USUARIO.toString().equals(rol)) {
            throw new WebException("Debe tener rol valido");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Usuario user = usuarioRepositorio.buscarPorEmail(correo);

        if (user != null) {
            List<GrantedAuthority> permissions = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + user.getRol().toString());
            permissions.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuario", user);
            return new org.springframework.security.core.userdetails.User(user.getCorreo(), user.getClave(),
                    permissions);
        }
        return null;

    }

}
