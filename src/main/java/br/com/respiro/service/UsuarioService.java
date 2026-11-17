package br.com.respiro.service;

import br.com.respiro.dto.*;
import br.com.respiro.exception.ApiException;
import br.com.respiro.model.Usuario;
import br.com.respiro.repository.SessaoRepository;
import br.com.respiro.repository.UsuarioRepository;
import br.com.respiro.util.Niveis;
import br.com.respiro.util.Tempo;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private static final String[] ROTULOS = {"S", "T", "Q", "Q", "S", "S", "D"};

    private final UsuarioRepository usuarios;
    private final SessaoRepository sessoes;

    public UsuarioService(UsuarioRepository usuarios, SessaoRepository sessoes) {
        this.usuarios = usuarios;
        this.sessoes = sessoes;
    }

    public Usuario buscar(long id) {
        return usuarios.porId(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public PerfilResponse perfil(long id) {
        Usuario u = buscar(id);
        List<SessaoResumo> recentes = sessoes.recentes(id, 5).stream()
                .map(s -> new SessaoResumo(s.minutos(), s.pontos(), s.dia())).toList();
        return new PerfilResponse(u.id(), u.nome(), u.email(), u.pontos(), Niveis.de(u.pontos()),
                u.metaDiariaHoras(), usuarios.interesses(id), u.onboardingOk(), recentes);
    }

    @Transactional
    public PerfilResponse onboarding(long id, OnboardingRequest r) {
        usuarios.atualizarOnboarding(id, r.horasIniciais(), r.metaDiariaHoras());
        usuarios.substituirInteresses(id, limpar(r.interesses()));
        return perfil(id);
    }

    public PerfilResponse atualizarMeta(long id, MetaRequest r) {
        usuarios.atualizarMeta(id, r.metaDiariaHoras());
        return perfil(id);
    }

    @Transactional
    public PerfilResponse atualizarInteresses(long id, InteressesRequest r) {
        usuarios.substituirInteresses(id, limpar(r.interesses()));
        return perfil(id);
    }

    public HomeResponse home(long id) {
        Usuario u = buscar(id);
        LocalDate hoje = Tempo.hoje();
        LocalDate segunda = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Set<LocalDate> feitos = new HashSet<>(sessoes.diasComSessao(id, segunda, segunda.plusDays(6)));

        List<DiaSemana> dias = IntStream.range(0, 7).mapToObj(i -> {
            LocalDate d = segunda.plusDays(i);
            return new DiaSemana(ROTULOS[i], feitos.contains(d), d.equals(hoje));
        }).toList();

        int sequencia = u.sequenciaAtual(hoje);
        return new HomeResponse(u.nome(), u.metaDiariaHoras(), sessoes.minutosDoDia(id, hoje),
                feitos.size() * 100 / 7, dias, u.pontos(), Niveis.de(u.pontos()), sequencia,
                Math.max(0, u.melhorSequencia() - sequencia));
    }

    private List<String> limpar(List<String> nomes) {
        return nomes.stream().map(String::trim).filter(n -> !n.isEmpty()).distinct().toList();
    }
}
