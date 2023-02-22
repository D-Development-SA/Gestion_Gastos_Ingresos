package com.GestionGastosIngresos.Backend.Spring.Dao;

import com.GestionGastosIngresos.Backend.Spring.Entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
    Usuario findUsuarioByNombreContaining(String nombre);
}
