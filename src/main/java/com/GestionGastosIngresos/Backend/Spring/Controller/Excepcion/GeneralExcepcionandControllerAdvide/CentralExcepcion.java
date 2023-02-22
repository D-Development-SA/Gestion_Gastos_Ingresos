package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CentralExcepcion extends RuntimeException implements ICentralExcepcion{
    protected String code;
    protected String tipo;
    protected HttpStatus httpStatus;
    protected String valorField;

    public CentralExcepcion(String message, String code, String tipo, String valorField, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.tipo = tipo;
        this.valorField = valorField;
        this.httpStatus = httpStatus;
    }

}
