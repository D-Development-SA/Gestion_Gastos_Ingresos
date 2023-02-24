package com.GestionGastosIngresos.Backend.Spring.Controller;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.ArgumentNoValidExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.CamposVaciosExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.BDExcepcion.NotExistExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide.ListaEmptyExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Entity.GastoIngreso;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IGastoIngresoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gastosIngresos")
public class GastoIngresoController {
    @Autowired
    private IGastoIngresoService gastoIngresoService;

    @GetMapping("/ver")
    public ResponseEntity<List<GastoIngreso>> findAllGastoIngreso(){
        List<GastoIngreso> gastoIngreso = gastoIngresoService.findAll();
        if (gastoIngreso.isEmpty()) throw new ListaEmptyExcepcion("GastoIngreso", "Busqueda de todos los GastoIngreso");
        return new ResponseEntity<>(gastoIngreso, HttpStatus.OK);
    }

    @GetMapping("/buscarID/{id}")
    public ResponseEntity<GastoIngreso> findGastoIngresoById(@PathVariable Long id){
        GastoIngreso gastoIngresoAux = gastoIngresoService.findById(id);

        return new ResponseEntity<>(gastoIngresoAux, HttpStatus.OK);
    }

    private void validaListaGastoIngreso(List<GastoIngreso> gastoIngreso, String nombre) {
        if(gastoIngreso.isEmpty())
            throw new NotExistExcepcion(nombre, "GastoIngreso");
    }

    private void validaElParametroNombre(String nombre) {
        if(nombre.isEmpty()){
            throw new CamposVaciosExcepcion(nombre, "nombre");
        } else if (nombre.matches(".*[^a-zA-Z0-9].*")) {
            throw new ArgumentNoValidExcepcion(nombre, "nombre");
        }
    }

    @PostMapping("/crearGastoIngreso")
    public ResponseEntity<GastoIngreso> createGastoIngreso(@RequestBody @Valid GastoIngreso gastoIngreso){
        GastoIngreso gastoIngresoSalvado = gastoIngresoService.save(gastoIngreso);

        return new ResponseEntity<>(gastoIngresoSalvado, HttpStatus.CREATED);
    }

    @PutMapping("/actualizarGastoIngreso/{id}")
    public ResponseEntity<GastoIngreso> updateGastoIngreso(@RequestBody @Valid GastoIngreso gastoIngreso, @PathVariable long id){
        GastoIngreso gastoIngresoAux = gastoIngresoService.findById(id);

        gastoIngresoAux.setIngreso(gastoIngreso.getIngreso());
        gastoIngresoAux.setGasto(gastoIngreso.getGasto());

        GastoIngreso gastoIngresoSalvado = gastoIngresoService.save(gastoIngresoAux);

        return new ResponseEntity<>(gastoIngresoSalvado, HttpStatus.CREATED);
    }

    @DeleteMapping("deleteGastoIngreso/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGastoIngreso(@PathVariable Long id){
        GastoIngreso gastoIngresoAux = gastoIngresoService.findById(id);

        gastoIngresoService.deleteById(id);
    }

    @DeleteMapping("/deleteAllGastoIngreso")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllGastoIngreso(){
        gastoIngresoService.deleteAll();
    }
}
