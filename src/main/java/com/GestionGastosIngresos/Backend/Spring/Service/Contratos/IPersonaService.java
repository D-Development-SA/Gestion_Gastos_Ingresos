package com.GestionGastosIngresos.Backend.Spring.Service.Contratos;

import com.GestionGastosIngresos.Backend.Spring.Entity.Persona;

import java.util.List;

public interface IPersonaService extends IGenericServices<Persona>{
    List<Persona> findPersonasByPrimerNombreOrSegundoNombre(String primerNombre, String SegundoNombre);
    List<Persona> findPersonasByPrimerApellidoOrSegundoApellido(String primerApellido, String SegundoApellido);
}
