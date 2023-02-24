package com.GestionGastosIngresos.Backend.Spring.Security;

import com.GestionGastosIngresos.Backend.Spring.Entity.Rol;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IUsuarioService;
import com.GestionGastosIngresos.Backend.Spring.Service.Implementaciones.UsuarioImplementacion;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthCredenciales authCredenciales;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException{
        authCredenciales = new AuthCredenciales();

        try {
            authCredenciales = new ObjectMapper().readValue(request.getReader(), AuthCredenciales.class);
        } catch (IOException e) {
        }

        UsernamePasswordAuthenticationToken usernamePat = new UsernamePasswordAuthenticationToken(
                authCredenciales.getNombre(),
                authCredenciales.getContrasenna()
        );

        return getAuthenticationManager().authenticate(usernamePat);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        authCredenciales.setAuthorities(UsuarioImplementacion
                .obtenerUsuario(authCredenciales.getNombre())
                .getRoles()
                .stream()
                .map(Rol::getRol)
                .collect(Collectors.toList()));

        String token = Tokens.crearToken(authCredenciales.getNombre(), authCredenciales.getAuthorities());

        response.addHeader("Autorizacion", token);
        response.getWriter().flush();
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
