package com.GestionGastosIngresos.Backend.Spring.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.*;

public class Tokens {
    private final static String ACCESS_TOKEN_SECRET = "4qhq8LrEBfYcaRHxhdb9zURb2rf8e7Ud";

    public static String crearToken(String nombre, List<String> authorities){

        HashMap<String, Object> authority = new HashMap<>();

        for (int i = 0; i < authorities.size(); i++) {
            authority.put("rol"+(i+1), authorities.get(i));
        }

        return Jwts.builder()
                .setSubject(nombre)
                //.setExpiration()
                .addClaims(authority)
                .signWith(getSigningKey())
                .compact();
    }
    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes());
    }

    public static UsernamePasswordAuthenticationToken getAutenticacionPorToken(String token){
        try {
            List<GrantedAuthority> authorities = new ArrayList<>();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String nombre = claims.getSubject();

            HashMap<String, Object> extra = new HashMap<>(claims);

            extra.forEach((key, rol) -> authorities.add(new SimpleGrantedAuthority(rol.toString())));

            return new UsernamePasswordAuthenticationToken(nombre, null, authorities);
        } catch (JwtException e) {
            System.out.println("No se pudo obtener la autenticacion por token");
            return null;
        }
    }
}
