package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.BDExcepcion;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide.CentralExcepcion;
import org.springframework.http.HttpStatus;

public class AccessBDExcepcion extends CentralExcepcion {
    public AccessBDExcepcion() {
        super("No se pudo realizar la operacion a la BD correctamente",
                "BD-301",
                "ErrorQuery",
                "???",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
