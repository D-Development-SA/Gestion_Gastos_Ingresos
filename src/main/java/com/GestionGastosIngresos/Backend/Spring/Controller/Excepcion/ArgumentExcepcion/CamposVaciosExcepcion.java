package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide.CentralExcepcion;
import org.springframework.http.HttpStatus;

public class CamposVaciosExcepcion extends CentralExcepcion {
    public CamposVaciosExcepcion(String text, String valor) {
        super("Campo interpretado como vacio o nulo -> ["+text+"]",
                "C-100",
                "NullField",
                valor,
                HttpStatus.BAD_REQUEST);
    }
}
