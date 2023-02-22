package com.GestionGastosIngresos.Backend.Spring.Service.Contratos;

import com.GestionGastosIngresos.Backend.Spring.Entity.Usuario;

import java.util.List;

public interface IUsuarioService extends IGenericServices<Usuario>{
    Usuario findUsuarioByNombreContaining(String Nombre);
}
