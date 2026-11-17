package br.com.respiro.controller;

import br.com.respiro.dto.AuthResponse;
import br.com.respiro.dto.LoginRequest;
import br.com.respiro.dto.RegistroRequest;
import br.com.respiro.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/registrar")
    public AuthResponse registrar(@Valid @RequestBody RegistroRequest r) {
        return auth.registrar(r);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest r) {
        return auth.login(r);
    }
}
