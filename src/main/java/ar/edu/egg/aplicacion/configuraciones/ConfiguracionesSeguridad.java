package ar.edu.egg.aplicacion.configuraciones;

import ar.edu.egg.aplicacion.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfiguracionesSeguridad extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("usuarioServicio")
    public UsuarioServicio usuarioServicio;
    
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(usuarioServicio).
		passwordEncoder(new BCryptPasswordEncoder());
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()
				.and().formLogin()
					.loginPage("/login")
						.loginProcessingUrl("/logincheck")
						.usernameParameter("correo")
						.passwordParameter("clave")
						.defaultSuccessUrl("/loginsuccess")
						.failureUrl("/login?error=error")
						.permitAll()
				.and().logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
				.and().csrf()
					.disable();
	}
}
