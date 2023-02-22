package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide.CentralExcepcion;
import org.springframework.http.HttpStatus;

public class ArgumentNoValidExcepcion extends CentralExcepcion {
    public ArgumentNoValidExcepcion(String valorField, String text) {
        super("Exixtencia de argumento(s) no valido(s) -> ["+ text+ "]",
                "A-100",
                "ArgumentInvalid",
                valorField,
                HttpStatus.BAD_REQUEST);
    }
}
