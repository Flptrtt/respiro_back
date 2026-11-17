package br.com.respiro.dto;

import jakarta.validation.constraints.*;

public record DesafioRequest(
        @NotBlank(message = "Dê um nome ao desafio") @Size(max = 40) String nome,
        @Min(1) @Max(30) int duracaoDias,
        @Min(5) @Max(720) int metaDiariaMin) {}
