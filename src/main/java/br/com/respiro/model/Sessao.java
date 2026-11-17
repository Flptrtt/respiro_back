package br.com.respiro.model;

import java.time.LocalDate;

public record Sessao(long id, long usuarioId, int minutos, boolean presente, int pontos, LocalDate dia) {}
