package com.GestionGastosIngresos.Backend.Spring.Controller;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.ArgumentNoValidExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.CamposVaciosExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.BDExcepcion.NotExistExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide.ListaEmptyExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Entity.Usuario;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> findAllUsuario(){
        List<Usuario> usuario = usuarioService.findAll();
        if (usuario.isEmpty()) throw new ListaEmptyExcepcion("Usuarios", "Busqueda de todos los Usuarios");
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping("/buscarID/{id}")
    public ResponseEntity<Usuario> findUsuarioById(@PathVariable Long id){
        Usuario usuarioAux = usuarioService.findById(id);

        return new ResponseEntity<>(usuarioAux, HttpStatus.OK);
    }

    @GetMapping("/buscarNombre/{nombre}")
    public ResponseEntity<Usuario> findByName (@PathVariable String nombre){
        validaElParametroNombre(nombre);

        Usuario usuario = usuarioService.findUsuarioByNombreContaining(nombre);

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    private void validaElParametroNombre(String nombre) {
        if(nombre.isEmpty()){
            throw new CamposVaciosExcepcion(nombre, "nombre");
        } else if (nombre.matches(".*[^a-zA-Z0-9].*")) {
            throw new ArgumentNoValidExcepcion(nombre, "nombre");
        }
    }

    @PostMapping("/crearUsuario")
    public ResponseEntity<Usuario> createUsuario(@RequestBody @Valid Usuario usuario){
        Usuario usuarioSalvado = usuarioService.save(usuario);

        return new ResponseEntity<>(usuarioSalvado, HttpStatus.CREATED);
    }

    @PutMapping("/actualizarUsuario/{id}")
    public ResponseEntity<Usuario> updateUsuario(@RequestBody @Valid Usuario usuario, @PathVariable long id){
        Usuario usuarioAux = usuarioService.findById(id);

        usuarioAux.setContrasenna(usuario.getContrasenna());
        usuarioAux.setRoles(usuario.getRoles());
        usuarioAux.setNombre(usuario.getNombre());
        usuarioAux.setEnabled(usuario.isEnabled());

        Usuario usuarioSalvado = usuarioService.save(usuarioAux);

        return new ResponseEntity<>(usuarioSalvado, HttpStatus.CREATED);
    }

    @DeleteMapping("deleteUsuario/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUsuario(@PathVariable Long id){
        Usuario usuarioAux = usuarioService.findById(id);

        usuarioService.deleteById(id);
    }

    @DeleteMapping("/deleteAllUsuarios")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllUsuario(){
        usuarioService.deleteAll();
    }
}
