package com.GestionGastosIngresos.Backend.Spring.Service.Implementaciones;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.BDExcepcion.NotExistExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IGenericServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class GenericImplementacion <E, D extends CrudRepository<E, Long>> implements IGenericServices<E> {
    @Autowired
    protected final D dao;

    public GenericImplementacion(D dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<E> findAll() {
        return (List<E>) dao.findAll();
    }

    @Override
    @Transactional
    public E save(E daoArg) {
        return dao.save(daoArg);
    }

    @Override
    @Transactional(readOnly = true)
    public E findById(long id) {
        if(id == 0) throw new NotExistExcepcion(id, String.valueOf(id));
        E e = dao.findById(id).orElse(null);

        if(e == null) throw new NotExistExcepcion(id, String.valueOf(id));

        return e;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        dao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }
}
