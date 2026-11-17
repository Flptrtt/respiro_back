package br.com.respiro.dto;

/** status: ATIVO, CONCLUIDO ou ENCERRADO (prazo acabou sem completar). */
public record DesafioResponse(long id, String nome, String descricao, int diasRestantes,
                              int percentual, String status) {}
