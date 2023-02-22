package com.GestionGastosIngresos.Backend.Spring.Service.Contratos;

import java.util.List;

public interface IGenericServices <E>{
    List<E> findAll();
    E save(E entity);
    E findById(long id);
    void deleteById(long id);
    void deleteAll();
}
