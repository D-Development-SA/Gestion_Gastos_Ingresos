package com.GestionGastosIngresos.Backend.Spring.Service.Implementaciones;

import com.GestionGastosIngresos.Backend.Spring.Dao.IUsuarioDao;
import com.GestionGastosIngresos.Backend.Spring.Entity.Usuario;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioImplementacion extends GenericImplementacion<Usuario, IUsuarioDao> implements UserDetailsService, IUsuarioService {
    private Logger logger = LoggerFactory.getLogger(UsuarioImplementacion.class);
    @Autowired
    public UsuarioImplementacion(IUsuarioDao dao) {
        super(dao);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = dao.findUsuarioByNombreContaining(username);

        if(usuario == null){
            logger.error("Error en el login: no existe ususario con el nombre '"+username+"' en el sistema");
            throw new UsernameNotFoundException("Error en el login: no existe ususario con el nombre '"+username+"' en el sistema");
        }

        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getRol()))
                .peek(authority -> logger.info("Role: " + authority.getAuthority()))
                .collect(Collectors.toList());

        return new User(usuario.getNombre(), usuario.getContrasenna(), usuario.isEnabled(),
                true, true, true, authorities);
    }

    @Override
    public Usuario findUsuarioByNombreContaining(String Nombre) {
        return dao.findUsuarioByNombreContaining(Nombre);
    }
}
