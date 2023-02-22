package com.GestionGastosIngresos.Backend.Spring.Security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
public class AuthCredenciales {
    private String nombre;
    private String contrasenna;
    private List<GrantedAuthority> authorities;
}
