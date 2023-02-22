package com.GestionGastosIngresos.Backend.Spring.Controller;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.ArgumentNoValidExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.CamposVaciosExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.BDExcepcion.NotExistExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide.ListaEmptyExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Entity.Persona;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IPersonaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    @Autowired
    private IPersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> findAllPersona(){
        List<Persona> personas = personaService.findAll();
        if (personas.isEmpty()) throw new ListaEmptyExcepcion("Personas", "Busqueda de todas las Personas");
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    @GetMapping("/buscarID/{id}")
    public ResponseEntity<Persona> findPersonaById(@PathVariable Long id){
        Persona personaAux = personaService.findById(id);

        return new ResponseEntity<>(personaAux, HttpStatus.OK);
    }

    @GetMapping("/buscarNombre/{nombre}")
    public ResponseEntity<List<Persona>> findByName (@PathVariable String nombre){
        validaElParametroNombre(nombre, "nombre");

        List<Persona> personas = personaService.findPersonasByPrimerNombreOrSegundoNombre(nombre, nombre);

        validaListaPersonas(personas, nombre);

        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    private void validaListaPersonas(List<Persona> personas, String nombre) {
        if(personas.isEmpty())
            throw new NotExistExcepcion(nombre, "Personas");
    }

    private void validaElParametroNombre(String nombre, String campo) {
        if(nombre.isEmpty()){
            throw new CamposVaciosExcepcion(nombre, campo);
        } else if (nombre.matches(".*[^a-zA-Z0-9].*")) {
            throw new ArgumentNoValidExcepcion(nombre, campo);
        }
    }

    @GetMapping("/buscarApellido/{apellido}")
    public ResponseEntity<List<Persona>> findByApellido (@PathVariable String apellido){
        validaElParametroNombre(apellido, "apellido");

        List<Persona> personas = personaService.findPersonasByPrimerApellidoOrSegundoApellido(apellido, apellido);

        validaListaPersonas(personas, apellido);

        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    @PostMapping("/crearPersona")
    public ResponseEntity<Persona> createPersona(@RequestBody @Valid Persona persona){
        Persona personaSalvada = personaService.save(persona);

        return new ResponseEntity<>(personaSalvada, HttpStatus.CREATED);
    }

    @PutMapping("/actualizarPersona/{id}")
    public ResponseEntity<Persona> updatePersona(@RequestBody @Valid Persona persona, @PathVariable long id){
        Persona personaAux = personaService.findById(id);

        personaAux.setPrimerNombre(persona.getPrimerNombre());
        personaAux.setSegundoNombre(persona.getSegundoNombre());
        personaAux.setPrimerApellido(persona.getPrimerApellido());
        personaAux.setSegundoApellido(persona.getSegundoApellido());
        personaAux.setUsuario(personaAux.getUsuario());

        Persona personaSalvada = personaService.save(personaAux);

        return new ResponseEntity<>(personaSalvada, HttpStatus.CREATED);
    }

    @DeleteMapping("deletePersona/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePersona(@PathVariable Long id){
        Persona personaAux = personaService.findById(id);

        personaService.deleteById(id);
    }

    @DeleteMapping("/deleteAllPersonas")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllPersona(){
        personaService.deleteAll();
    }

}
