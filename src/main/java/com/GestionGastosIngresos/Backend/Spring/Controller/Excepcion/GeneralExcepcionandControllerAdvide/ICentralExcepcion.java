package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide;

import org.springframework.http.HttpStatus;

public interface ICentralExcepcion {
    String getCode();
    String getTipo();
    String getMessage();
    HttpStatus getHttpStatus();
    String getValorField();
}
