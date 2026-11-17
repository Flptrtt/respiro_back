package br.com.respiro.dto;

import java.util.List;

public record PerfilResponse(long id, String nome, String email, int pontos, NivelResponse nivel,
                             int metaDiariaHoras, List<String> interesses, boolean onboardingOk,
                             List<SessaoResumo> sessoesRecentes) {}
