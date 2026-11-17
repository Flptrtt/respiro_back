package br.com.respiro.service;

import br.com.respiro.dto.SessaoRequest;
import br.com.respiro.dto.SessaoResultado;
import br.com.respiro.model.Usuario;
import br.com.respiro.repository.DesafioRepository;
import br.com.respiro.repository.SessaoRepository;
import br.com.respiro.repository.UsuarioRepository;
import br.com.respiro.util.Niveis;
import br.com.respiro.util.Tempo;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessaoService {
    private static final int PONTOS_POR_MINUTO = 3;
    private static final int BONUS_PRESENTE = 15;

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarios;
    private final SessaoRepository sessoes;
    private final DesafioRepository desafios;

    public SessaoService(UsuarioService usuarioService, UsuarioRepository usuarios,
                         SessaoRepository sessoes, DesafioRepository desafios) {
        this.usuarioService = usuarioService;
        this.usuarios = usuarios;
        this.sessoes = sessoes;
        this.desafios = desafios;
    }

    @Transactional
    public SessaoResultado finalizar(long usuarioId, SessaoRequest r) {
        Usuario u = usuarioService.buscar(usuarioId);
        LocalDate hoje = Tempo.hoje();

        int pontos = r.minutos() * PONTOS_POR_MINUTO + (r.presente() ? BONUS_PRESENTE : 0);
        sessoes.inserir(usuarioId, r.minutos(), r.presente(), pontos, hoje);
        desafios.somarProgresso(usuarioId, r.minutos(), hoje);

        int sequencia = novaSequencia(u, hoje);
        int melhor = Math.max(u.melhorSequencia(), sequencia);
        int total = u.pontos() + pontos;
        usuarios.atualizarProgresso(usuarioId, total, sequencia, melhor, hoje);

        return new SessaoResultado(r.minutos(), pontos, total, sequencia, Niveis.de(total));
    }

    private int novaSequencia(Usuario u, LocalDate hoje) {
        LocalDate ultimo = u.ultimoDiaAtivo();
        if (ultimo != null && ultimo.equals(hoje)) return Math.max(1, u.sequencia());
        if (ultimo != null && ultimo.equals(hoje.minusDays(1))) return u.sequencia() + 1;
        return 1;
    }
}
