package com.GestionGastosIngresos.Backend.Spring.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

public class Tokens {
    private final static String ACCESS_TOKEN_SECRET = "DAUWNlnUNKuHUkisjo8w";
    private final static long ACCESS_TOKENS_VALIDATION_TOKEN = 24 * 30 * 3600;

    public static String crearToken(String nombre, String contrasenna, Collection<? extends GrantedAuthority> authority){

        long tiempoExpiracion = ACCESS_TOKENS_VALIDATION_TOKEN * 1000;

        HashMap<String, Object> extra = new HashMap<>();

        extra.put("contrasenna", contrasenna);
        extra.put("autoritis", authority);

        return Jwts.builder()
                .setSubject(nombre)
                .setExpiration(new Date(tiempoExpiracion))
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAutenticacionPorToken(String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String nombre = claims.getSubject();
            Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)claims.get("autoritis");

            return new UsernamePasswordAuthenticationToken(nombre, null, authorities);
        } catch (JwtException e) {
            return null;
        }
    }
}
