package com.GestionGastosIngresos.Backend.Spring.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "usuarios")
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "No puede estar vacio el Nombre")
    @NotNull(message = "No puede ser nulo el Nombre")
    @Size(max = 20, min = 1, message = "El nombre no cumple con los requisitos, maximo 20 caracteres")
    @Column(nullable = false, unique = true, length = 20)
    private String nombre;
    @NotEmpty(message = "No puede estar vacia la contrasenna")
    @NotNull(message = "No puede ser nula la contrasenna")
    @Size(max = 60, min = 10, message = "La contrasenna no cumple con los requisitos, minimo 10 caracteres")
    @Column(nullable = false, unique = true, length = 60)
    private String contrasenna;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "rol_id"}))
    private List<Rol> roles;
}
