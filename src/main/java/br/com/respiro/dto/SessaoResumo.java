package br.com.respiro.dto;

import java.time.LocalDate;

public record SessaoResumo(int minutos, int pontos, LocalDate dia) {}
