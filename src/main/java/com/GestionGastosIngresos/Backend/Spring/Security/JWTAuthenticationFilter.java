package com.GestionGastosIngresos.Backend.Spring.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException{
        AuthCredenciales authCredenciales = new AuthCredenciales();

        try {
            authCredenciales = new ObjectMapper().readValue(request.getReader(), AuthCredenciales.class);
        } catch (IOException e) {
        }

        UsernamePasswordAuthenticationToken usernamePat = new UsernamePasswordAuthenticationToken(
                authCredenciales.getNombre(),
                authCredenciales.getContrasenna(),
                authCredenciales.getAuthorities()
        );

        return getAuthenticationManager().authenticate(usernamePat);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailsImplementacion userDetailsImpl = (UserDetailsImplementacion) authResult.getPrincipal();
        String token = Tokens.crearToken(userDetailsImpl.getUsername(), userDetailsImpl.getPassword(), userDetailsImpl.getAuthorities());

        response.addHeader("Autorizacion", "Bearer " + token);
        response.getWriter().flush();
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
