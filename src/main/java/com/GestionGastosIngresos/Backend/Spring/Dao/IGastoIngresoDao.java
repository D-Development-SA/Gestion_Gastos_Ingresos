package com.GestionGastosIngresos.Backend.Spring.Dao;

import com.GestionGastosIngresos.Backend.Spring.Entity.GastoIngreso;
import org.springframework.data.repository.CrudRepository;

public interface IGastoIngresoDao extends CrudRepository<GastoIngreso, Long> {
}
