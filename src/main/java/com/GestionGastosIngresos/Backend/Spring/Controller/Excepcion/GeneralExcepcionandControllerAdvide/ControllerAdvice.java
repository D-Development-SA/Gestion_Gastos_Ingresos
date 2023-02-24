package com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide;

import com.GestionGastosIngresos.Backend.Spring.Controller.Errors.Errors;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.ArgumentNoValidExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.CamposIncorrectosExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.CamposVaciosExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.FechaIncorrectaException;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.BDExcepcion.AccessBDExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.BDExcepcion.NotExistExcepcion;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Objects;

@RestControllerAdvice
public class ControllerAdvice {
    private HashMap<String, String> hashMap;
    @ExceptionHandler(value = {
            NotExistExcepcion.class,
            ListaEmptyExcepcion.class,
            CamposVaciosExcepcion.class,
            NotExistExcepcion.class,
            ListaEmptyExcepcion.class,
            FechaIncorrectaException.class
    })
    public ResponseEntity<Errors> handlerFieldInc(ICentralExcepcion ex){
        Errors e = getBuild(ex, null);

        return new ResponseEntity<>(e, ex.getHttpStatus());
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Errors> handlerValidateMethod(MethodArgumentNotValidException ex){
        CamposIncorrectosExcepcion camposIncorrectosExcepcion;
        hashMap = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> hashMap.put(((FieldError)error).getField(), error.getDefaultMessage()));

        camposIncorrectosExcepcion = new CamposIncorrectosExcepcion(hashMap.size()+" campos incorrectos", "???");

        Errors e = getBuild(camposIncorrectosExcepcion, hashMap);

        return new ResponseEntity<>(e, camposIncorrectosExcepcion.getHttpStatus());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Errors> argumentosNoValid(MethodArgumentTypeMismatchException ex){
        ArgumentNoValidExcepcion argumentNoValidExcepcion = new ArgumentNoValidExcepcion(ex.getParameter().getParameterName(), Objects.requireNonNull(ex.getValue()).toString());
        hashMap = new HashMap<>();

        hashMap.put("Info", ex.getLocalizedMessage().split(";")[0]);
        hashMap.put("Campo", ex.getName());
        hashMap.put("Causa", ex.getCause().toString().split(":")[0]);
        hashMap.put("Info1", ex.getParameter().toString());
        hashMap.put("Valor", Objects.requireNonNull(ex.getValue()).toString());

        Errors e = getBuild(argumentNoValidExcepcion, hashMap);

        return new ResponseEntity<>(e, argumentNoValidExcepcion.getHttpStatus());
    }

    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<Errors> noAccessBD(DataAccessException ex){
        hashMap = new HashMap<>();
        AccessBDExcepcion accessBDExcepcion = new AccessBDExcepcion();

        hashMap.put("Info", "No se pudo realizar la consulta a la BD correctamente");
        hashMap.put("Error", ex.getLocalizedMessage().split(";")[0]);
        hashMap.put("Causa", ex.getCause().toString().split(":")[0]);
        hashMap.put("Info1", ex.getMessage());

        Errors e = getBuild(accessBDExcepcion, hashMap);

        return new ResponseEntity<>(e, accessBDExcepcion.getHttpStatus());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Errors> handlerExcepcion(Exception ex){
        ErrorInesperadoExcepcion excepcion = new ErrorInesperadoExcepcion();
        String localizedMessage;
        String message;
        String causeMessage;

        if(ex.getLocalizedMessage() != null)
            localizedMessage = ex.getLocalizedMessage();
        else localizedMessage = "No tiene";
        if(ex.getMessage() != null)
            message = ex.getMessage();
        else message = "No tiene";
        if(ex.getCause() != null && ex.getCause().getLocalizedMessage() != null)
            causeMessage = ex.getCause().getMessage();
        else causeMessage = "No tiene";

        hashMap = new HashMap<>();

        hashMap.put("Error", "Error inesperado");
        hashMap.put("Mensaje", message);
        hashMap.put("Mensaje especificado", localizedMessage);
        hashMap.put("Causa",causeMessage);

        Errors e = getBuild(excepcion, hashMap);

        return new ResponseEntity<>(e, excepcion.getHttpStatus());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<HashMap<String, String>> handlerRuntime(RuntimeException ex){
        String localizedMessage;
        String message;
        String causeMessage;

        if(ex.getLocalizedMessage() != null)
            localizedMessage = ex.getLocalizedMessage();
        else localizedMessage = "No tiene";
        if(ex.getMessage() != null)
            message = ex.getMessage();
        else message = "No tiene";
        if(ex.getCause() != null && ex.getCause().getLocalizedMessage() != null)
            causeMessage = ex.getCause().getMessage();
        else causeMessage = "No tiene";

        System.out.println(localizedMessage);
        System.out.println(message);
        System.out.println(causeMessage);

        hashMap = new HashMap<>();

        hashMap.put("Error", "Interrupcion recibida");
        hashMap.put("Mensaje", message);
        hashMap.put("Mensaje especificado", localizedMessage);
        hashMap.put("Causa", causeMessage);

        return new ResponseEntity<>(hashMap, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<HashMap<String, String>> handlerUsername(UsernameNotFoundException ex){
        hashMap = new HashMap<>();

        hashMap.put("Error", "Login");
        hashMap.put("Mensaje", ex.getMessage());
        hashMap.put("Mensaje especificado", ex.getLocalizedMessage());
        hashMap.put("Causa", ex.getCause().getLocalizedMessage());

        return new ResponseEntity<>(hashMap, HttpStatus.BAD_REQUEST);
    }

    private static Errors getBuild(ICentralExcepcion centralExcepcion, HashMap<String, String> hashMap) {
        return Errors.builder()
                .code(centralExcepcion.getCode())
                .tipo(centralExcepcion.getTipo())
                .message(centralExcepcion.getMessage())
                .valor(centralExcepcion.getValorField())
                .errores(hashMap)
                .build();
    }
}
