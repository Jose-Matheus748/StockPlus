package com.unifor.stockPlus.controller;

import com.unifor.stockPlus.dto.LoginRequest;
import com.unifor.stockPlus.dto.UsuarioDTO;
import com.unifor.stockPlus.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public UsuarioDTO login(@RequestBody LoginRequest request) {
        return usuarioService.login(request.email(), request.senha());
    }
}
