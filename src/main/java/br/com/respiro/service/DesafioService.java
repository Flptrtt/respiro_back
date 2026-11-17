package br.com.respiro.service;

import br.com.respiro.dto.DesafioRequest;
import br.com.respiro.dto.DesafioResponse;
import br.com.respiro.model.Desafio;
import br.com.respiro.repository.DesafioRepository;
import br.com.respiro.util.Tempo;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DesafioService {
    private final DesafioRepository desafios;

    public DesafioService(DesafioRepository desafios) {
        this.desafios = desafios;
    }

    public List<DesafioResponse> listar(long usuarioId) {
        LocalDate hoje = Tempo.hoje();
        return desafios.listar(usuarioId).stream().map(d -> toResponse(d, hoje)).toList();
    }

    public DesafioResponse criar(long usuarioId, DesafioRequest r) {
        LocalDate hoje = Tempo.hoje();
        desafios.inserir(usuarioId, r.nome().trim(), r.duracaoDias(), r.metaDiariaMin(), hoje);
        return listar(usuarioId).get(0); // o mais recente vem primeiro
    }

    private DesafioResponse toResponse(Desafio d, LocalDate hoje) {
        int total = d.duracaoDias() * d.metaDiariaMin();
        int percentual = Math.min(100, d.progressoMin() * 100 / total);
        int restantes = (int) Math.max(0, d.duracaoDias() - ChronoUnit.DAYS.between(d.criadoEm(), hoje));
        String status = percentual >= 100 ? "CONCLUIDO" : restantes == 0 ? "ENCERRADO" : "ATIVO";
        return new DesafioResponse(d.id(), d.nome(), descricao(d.metaDiariaMin()), restantes, percentual, status);
    }

    private String descricao(int min) {
        String t = min < 60 ? min + " min" : (min % 60 == 0 ? min / 60 + "h" : min / 60 + "h " + min % 60 + "min");
        return t + " offline por dia";
    }
}
