package br.com.respiro.dto;

import java.util.List;

public record HomeResponse(String nome, int metaDiariaHoras, int minutosHoje, int percentualSemana,
                           List<DiaSemana> diasSemana, int pontos, NivelResponse nivel,
                           int sequencia, int diasParaMelhor) {}
