package com.GestionGastosIngresos.Backend.Spring.Service.Implementaciones;

import com.GestionGastosIngresos.Backend.Spring.Dao.IPersonaDao;
import com.GestionGastosIngresos.Backend.Spring.Entity.Persona;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonaImplementacion extends GenericImplementacion<Persona, IPersonaDao> implements IPersonaService {
    @Autowired
    public PersonaImplementacion(IPersonaDao dao) {
        super(dao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Persona> findPersonasByPrimerNombreOrSegundoNombre(String primerNombre, String segundoNombre) {
        return dao.findPersonasByPrimerNombreOrSegundoNombre(primerNombre, segundoNombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Persona> findPersonasByPrimerApellidoOrSegundoApellido(String primerApellido, String segundoApellido) {
        return dao.findPersonasByPrimerApellidoOrSegundoApellido(primerApellido, segundoApellido);
    }
}
