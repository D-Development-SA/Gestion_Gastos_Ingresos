package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide.CentralExcepcion;
import org.springframework.http.HttpStatus;

public class CamposIncorrectosExcepcion extends CentralExcepcion {
    public CamposIncorrectosExcepcion(String text, String valor) {
        super("Existencia de un campo con caracteres incorrectos no permitidos -> ["+text+"]",
                "C-101",
                "ArgumentsInval",
                valor,
                HttpStatus.BAD_REQUEST);
    }
    public CamposIncorrectosExcepcion(String text) {
        super("Existencia de un campo con caracteres incorrectos no permitidos -> ["+text+"], \n" +
                        "si contiene caracteres especiales puede solicitar una exclusion",
                "C-101",
                "ArgumentsInval",
                text,
                HttpStatus.BAD_REQUEST);
    }
}
