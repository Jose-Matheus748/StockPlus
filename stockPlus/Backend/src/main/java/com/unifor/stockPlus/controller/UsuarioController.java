package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.UsuarioDTO;
import com.unifor.stockPlus.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.unifor.stockPlus.dto.LoginRequest;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public UsuarioDTO create(@RequestBody UsuarioDTO dto) {
        return usuarioService.create(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody LoginRequest request) {
        UsuarioDTO usuario = usuarioService.login(request.email(), request.senha());
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{id}")
    public UsuarioDTO get(@PathVariable Long id) {
        return usuarioService.getById(id);
    }

    @GetMapping
    public List<UsuarioDTO> getAll() {
        return usuarioService.getAll();
    }

    @PutMapping("/{id}")
    public UsuarioDTO update(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return usuarioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        usuarioService.delete(id);
    }
}
