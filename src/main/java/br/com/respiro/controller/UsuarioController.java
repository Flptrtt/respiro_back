package br.com.respiro.controller;

import br.com.respiro.dto.*;
import br.com.respiro.service.SugestaoService;
import br.com.respiro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UsuarioController {
    private final UsuarioService usuarios;
    private final SugestaoService sugestoes;

    public UsuarioController(UsuarioService usuarios, SugestaoService sugestoes) {
        this.usuarios = usuarios;
        this.sugestoes = sugestoes;
    }

    @GetMapping("/me")
    public PerfilResponse me(@RequestAttribute("usuarioId") Long id) {
        return usuarios.perfil(id);
    }

    @PutMapping("/me/onboarding")
    public PerfilResponse onboarding(@RequestAttribute("usuarioId") Long id, @Valid @RequestBody OnboardingRequest r) {
        return usuarios.onboarding(id, r);
    }

    @PutMapping("/me/meta")
    public PerfilResponse meta(@RequestAttribute("usuarioId") Long id, @Valid @RequestBody MetaRequest r) {
        return usuarios.atualizarMeta(id, r);
    }

    @PutMapping("/me/interesses")
    public PerfilResponse interesses(@RequestAttribute("usuarioId") Long id, @Valid @RequestBody InteressesRequest r) {
        return usuarios.atualizarInteresses(id, r);
    }

    @GetMapping("/home")
    public HomeResponse home(@RequestAttribute("usuarioId") Long id) {
        return usuarios.home(id);
    }

    @GetMapping("/sugestao")
    public SugestaoResponse sugestao(@RequestAttribute("usuarioId") Long id) {
        return sugestoes.doDia(id);
    }
}
