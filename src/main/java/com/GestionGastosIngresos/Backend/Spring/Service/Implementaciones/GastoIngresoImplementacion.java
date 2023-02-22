package com.GestionGastosIngresos.Backend.Spring.Service.Implementaciones;

import com.GestionGastosIngresos.Backend.Spring.Dao.IGastoIngresoDao;
import com.GestionGastosIngresos.Backend.Spring.Entity.GastoIngreso;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IGastoIngresoService;
import org.springframework.stereotype.Service;

@Service
public class GastoIngresoImplementacion extends GenericImplementacion<GastoIngreso, IGastoIngresoDao> implements IGastoIngresoService {
    public GastoIngresoImplementacion(IGastoIngresoDao dao) {
        super(dao);
    }
}
