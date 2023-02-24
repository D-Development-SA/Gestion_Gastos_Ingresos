package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide.CentralExcepcion;
import org.springframework.http.HttpStatus;

public class FechaIncorrectaException extends CentralExcepcion {
    public FechaIncorrectaException(String valorField) {
        super("La fecha insertada es incorrecta, pruebe con el formato{dd-mm-yyyy} -> ["+valorField+"]",
                "F-100",
                "FechaIncorrecta",
                valorField,
                HttpStatus.BAD_REQUEST);
    }
}
