package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.BDExcepcion;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide.CentralExcepcion;
import org.springframework.http.HttpStatus;

public class NotExistExcepcion extends CentralExcepcion {
    public NotExistExcepcion(Long id, String valor) {
        super("No existe ningun elemento con el id especificado en la BD -> ["+id+"]",
                "BD-300",
                "NotExistInBD",
                valor,
                HttpStatus.NOT_FOUND);
    }
    public NotExistExcepcion(String text, String valor) {
        super("No existe ningun elemento con el campo especificado en la BD -> ["+text+"]",
                "BD-300",
                "NotExistInBD",
                valor,
                HttpStatus.NOT_FOUND);
    }
}
