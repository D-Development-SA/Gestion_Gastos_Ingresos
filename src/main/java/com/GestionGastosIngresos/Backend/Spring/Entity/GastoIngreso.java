package com.GestionGastosIngresos.Backend.Spring.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gastosIngresos")
public class GastoIngreso implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "\\d+.\\d+", message = "El valor ingresado en gast es incorrecto")
    @Column
    private double gasto;

    @Pattern(regexp = "\\d+.\\d+", message = "El valor ingresado en gasto es incorrecto")
    @Column
    private double ingreso;
    @NotNull(message = "No puede ser nula la fecha")
    @Pattern(regexp = "[\\d]{2}/[\\d]{2}/[\\d]{4}", message = "El valor ingresado es incorrecto")
    @Column(nullable = false, updatable = false)
    private String fecha;

    @PrePersist
    private void generarFechaHora(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fecha = formatter.format(LocalDateTime.now());
    }
}
