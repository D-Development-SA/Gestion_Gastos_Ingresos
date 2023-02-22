package com.GestionGastosIngresos.Backend.Spring.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JWTAutorizacionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("dofilterIntaernal");

        String bearerToken = request.getHeader("Autorizacion");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            String token = bearerToken.replace("Bearer ", "");

            UsernamePasswordAuthenticationToken userToken = Tokens.getAutenticacionPorToken(token);

            SecurityContextHolder.getContext().setAuthentication(userToken);
        }
        filterChain.doFilter(request, response);
    }
}
