package br.com.respiro.model;

import java.time.LocalDate;

public record Desafio(long id, long usuarioId, String nome, int duracaoDias, int metaDiariaMin,
                      int progressoMin, LocalDate criadoEm) {}
