package com.GestionGastosIngresos.Backend.Spring.Dao;

import com.GestionGastosIngresos.Backend.Spring.Entity.Persona;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPersonaDao extends CrudRepository<Persona, Long> {
    List<Persona> findPersonasByPrimerNombreOrSegundoNombre(String primerNombre, String segundoNombre);
    List<Persona> findPersonasByPrimerApellidoOrSegundoApellido(String primerApellido, String segundoApellido);
}
