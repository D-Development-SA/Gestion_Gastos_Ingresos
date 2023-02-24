package com.GestionGastosIngresos.Backend.Spring.Security;

import com.GestionGastosIngresos.Backend.Spring.Service.Implementaciones.UsuarioImplementacion;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
public class WebSecurityConfig {

    private final UsuarioImplementacion usuarioImplementacion;
    private final JWTAutorizacionFilter jwtAutorizacionFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager manager) throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(manager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        return httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/usuarios/crearUsuario")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/PDF/cargarPdf/**")
                .permitAll()
                .requestMatchers("/api/usuarios", "/api/personas")
                .hasRole("ADMIN")
                .requestMatchers("/api/usuarios/**", "/api/personas/**")
                .hasRole("ADMIN")
                .requestMatchers("/api/gastosIngresos/**", "/api/PDF/**")
                .hasAnyRole("ADMIN", "USER")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAutorizacionFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(usuarioImplementacion)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        String [] nombre = {
                "jas.pdf",
                "jas.pdf.pdf",
                "jas.pdfpdf",
                "jas.pdfpdf",
                "jas.fdp",
                "jas.jpg"
        };
        Arrays.stream(nombre).forEach(s -> System.out.println(s.split("\\.", 2)[1].matches("pdf")));
    }
}
