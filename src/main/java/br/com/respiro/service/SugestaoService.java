package br.com.respiro.service;

import br.com.respiro.dto.SugestaoResponse;
import br.com.respiro.repository.UsuarioRepository;
import br.com.respiro.util.Tempo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/** Sugere uma atividade offline com base nos interesses; muda a cada dia. */
@Service
public class SugestaoService {
    private static final SugestaoResponse PADRAO = new SugestaoResponse("Que tal uma caminhada?",
            "Dê uma volta no quarteirão sem o celular. Só você, o ritmo dos passos e o que está ao redor.", "sun");

    private static final Map<String, List<SugestaoResponse>> POR_INTERESSE = Map.of(
            "Fotografia", List.of(
                    new SugestaoResponse("Que tal fotografar algo ao seu redor?",
                            "Saia para um passeio curto e capture o que chama atenção. Sem filtros, sem redes sociais — só você e o momento.", "camera"),
                    new SugestaoResponse("Caçada de luz",
                            "Procure três lugares onde a luz esteja bonita agora e registre cada um com calma.", "camera")),
            "Leitura", List.of(
                    new SugestaoResponse("Vinte páginas de um livro",
                            "Escolha um livro e leia sem pressa. Quando o tempo acabar, anote uma frase que ficou.", "book-open")),
            "Música", List.of(
                    new SugestaoResponse("Ouça um disco inteiro",
                            "Escolha um álbum e ouça do início ao fim, sem pular faixa e sem olhar a tela.", "music")),
            "Edição de vídeo", List.of(
                    new SugestaoResponse("Planeje seu próximo vídeo no papel",
                            "Rabisque o roteiro e os planos de câmera à mão. Editar fica mais fácil quando a ideia está clara.", "video")),
            "Culinária", List.of(
                    new SugestaoResponse("Cozinhe algo do zero",
                            "Escolha uma receita simples e prepare com calma. Cozinhar também é uma pausa.", "coffee")),
            "Jogos", List.of(
                    new SugestaoResponse("Jogo de cartas ou tabuleiro",
                            "Chame alguém para uma partida ou jogue paciência com um baralho de verdade.", "grid")),
            "Vídeos e séries", List.of(
                    new SugestaoResponse("Troque a tela por uma saída",
                            "Vá a um café, à praça ou ao cinema e deixe o celular guardado.", "film")),
            "Redes sociais", List.of(
                    new SugestaoResponse("Ligue para alguém",
                            "Em vez de curtir uma foto, converse de verdade com uma pessoa de quem você gosta.", "phone")));

    private final UsuarioRepository usuarios;

    public SugestaoService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    public SugestaoResponse doDia(long usuarioId) {
        List<SugestaoResponse> opcoes = new ArrayList<>();
        for (String interesse : usuarios.interesses(usuarioId)) {
            opcoes.addAll(POR_INTERESSE.getOrDefault(interesse, List.of()));
        }
        if (opcoes.isEmpty()) return PADRAO;
        return opcoes.get(Tempo.hoje().getDayOfYear() % opcoes.size());
    }
}
