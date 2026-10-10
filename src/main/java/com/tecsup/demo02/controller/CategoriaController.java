package com.tecsup.demo02.controller;

import com.tecsup.demo02.dto.CategoriaDTO;
import com.tecsup.demo02.model.Categoria;
import com.tecsup.demo02.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Categoria c = categoriaService.buscarPorId(id);

        if (c == null) {
            return ResponseEntity.status(404).body("Categoria no encontrada");
        }

        return ResponseEntity.ok(c);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Categoria>> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(categoriaService.buscarPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody CategoriaDTO dto) {

        if (categoriaService.existePorNombre(dto.getNombre())) {
            return ResponseEntity.status(409).body("La categoria ya existe");
        }

        Categoria c = new Categoria();
        c.setNombre(dto.getNombre());
        c.setDescripcion(dto.getDescripcion());

        Categoria guardado = categoriaService.guardar(c);

        return ResponseEntity.status(201).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody CategoriaDTO dto) {

        Categoria existente = categoriaService.buscarPorId(id);

        if (existente == null) {
            return ResponseEntity.status(404).body("Categoria no existe");
        }

        if (categoriaService.existePorNombre(dto.getNombre()) &&
            !existente.getNombre().equals(dto.getNombre())) {
            return ResponseEntity.status(409).body("La categoria ya existe");
        }

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());

        return ResponseEntity.ok(categoriaService.guardar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        Categoria c = categoriaService.buscarPorId(id);

        if (c == null) {
            return ResponseEntity.status(404).body("No existe");
        }

        categoriaService.eliminar(id);

        return ResponseEntity.ok("Eliminado correctamente");
    }
}