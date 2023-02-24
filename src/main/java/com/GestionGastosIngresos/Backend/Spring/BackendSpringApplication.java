package com.GestionGastosIngresos.Backend.Spring;

import com.GestionGastosIngresos.Backend.Spring.Entity.Rol;
import com.GestionGastosIngresos.Backend.Spring.Entity.Usuario;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BackendSpringApplication implements CommandLineRunner {
	@Autowired
	private IUsuarioService usuarioService;

	public static void main(String[] args) {
		SpringApplication.run(BackendSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (usuarioService.findUsuarioByNombreContaining("Admin") == null) {
			Usuario usuario = new Usuario();
			List<Rol> roles = new ArrayList<>();

			roles.add(new Rol("ROLE_ADMIN"));
			roles.add(new Rol("ROLE_USER"));

			usuario.setContrasenna(new BCryptPasswordEncoder().encode("ADMIN"));
			usuario.setNombre("Admin");
			usuario.setEnabled(true);
			usuario.setRoles(roles);

			usuarioService.save(usuario);
		}
	}
}
