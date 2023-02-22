package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide;

import org.springframework.http.HttpStatus;

public class ErrorInesperadoExcepcion extends CentralExcepcion{
    public ErrorInesperadoExcepcion() {
        super("Anomalia detectada",
                "AS-1500",
                "Anomalia",
                "Inexistente",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
