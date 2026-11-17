package br.com.respiro.controller;

import br.com.respiro.dto.DesafioRequest;
import br.com.respiro.dto.DesafioResponse;
import br.com.respiro.service.DesafioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/desafios")
public class DesafioController {
    private final DesafioService desafios;

    public DesafioController(DesafioService desafios) {
        this.desafios = desafios;
    }

    @GetMapping
    public List<DesafioResponse> listar(@RequestAttribute("usuarioId") Long id) {
        return desafios.listar(id);
    }

    @PostMapping
    public DesafioResponse criar(@RequestAttribute("usuarioId") Long id, @Valid @RequestBody DesafioRequest r) {
        return desafios.criar(id, r);
    }
}
