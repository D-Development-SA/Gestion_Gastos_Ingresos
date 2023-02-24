package com.GestionGastosIngresos.Backend.Spring.Security;

import lombok.Data;

import java.util.List;

@Data
public class AuthCredenciales {
    private String nombre;
    private String contrasenna;
    private List<String> authorities;
}
