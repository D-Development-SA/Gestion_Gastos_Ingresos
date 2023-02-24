package com.GestionGastosIngresos.Backend.Spring.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personas")
public class Persona implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;
    @NotEmpty(message = "No puede estar vacio el primer Nombre")
    @NotNull(message = "No puede ser nulo el primer Nombre")
    @Pattern(regexp = "^[A-Z][a-z]+", message = "El nombre no cumple con los requerimientos")
    @Column(nullable = false)
    private String primerNombre;
    @NotEmpty(message = "No puede estar vacio el segundo Nombre")
    @NotNull(message = "No puede ser nulo el segundo Nombre")
    @Pattern(regexp = "^[A-Z][a-z]+", message = "El nombre no cumple con los requerimientos")
    @Column
    private String segundoNombre;
    @NotEmpty(message = "No puede estar vacio el primer Apellido")
    @NotNull(message = "No puede ser nulo el primer Apellido")
    @Pattern(regexp = "^[A-Z][a-z]+", message = "El apellido no cumple con los requerimientos")
    @Column(nullable = false)
    private String primerApellido;
    @NotEmpty(message = "No puede estar vacio el segundo Apellido")
    @NotNull(message = "No puede ser nulo el segundo Apellido")
    @Pattern(regexp = "^[A-Z][a-z]+", message = "El apellido no cumple con los requerimientos")
    @Column(nullable = false)
    private String segundoApellido;

    @NotNull(message = "El usuario no puede ser nulo")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Usuario usuario;

    @NotNull(message = "El usuario no puede ser nulo")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GastoIngreso> gastoIngreso;
}
