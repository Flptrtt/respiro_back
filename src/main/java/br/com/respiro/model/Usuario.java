package br.com.respiro.model;

import java.time.LocalDate;

public record Usuario(long id, String nome, String email, String senhaHash, int horasIniciais,
                      int metaDiariaHoras, int pontos, int sequencia, int melhorSequencia,
                      LocalDate ultimoDiaAtivo, boolean onboardingOk) {

    /** A sequência só vale se a última atividade foi hoje ou ontem. */
    public int sequenciaAtual(LocalDate hoje) {
        return ultimoDiaAtivo != null && !ultimoDiaAtivo.isBefore(hoje.minusDays(1)) ? sequencia : 0;
    }
}
