package br.com.respiro.controller;

import br.com.respiro.dto.SessaoRequest;
import br.com.respiro.dto.SessaoResultado;
import br.com.respiro.service.SessaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessoes")
public class SessaoController {
    private final SessaoService sessoes;

    public SessaoController(SessaoService sessoes) {
        this.sessoes = sessoes;
    }

    @PostMapping
    public SessaoResultado finalizar(@RequestAttribute("usuarioId") Long id, @Valid @RequestBody SessaoRequest r) {
        return sessoes.finalizar(id, r);
    }
}
