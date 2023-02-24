package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide;

import org.springframework.http.HttpStatus;

public class ListaEmptyExcepcion extends CentralExcepcion{
    public ListaEmptyExcepcion(String text, String valor) {
        super("No existe elementos en la BD con respecto a la peticion -> ["+text+"]",
                "L-600",
                "ListEmpty",
                valor,
                HttpStatus.BAD_REQUEST);
    }
}
