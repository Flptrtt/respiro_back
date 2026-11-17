package br.com.respiro.dto;

import jakarta.validation.constraints.*;

public record SessaoRequest(@Min(1) @Max(720) int minutos, boolean presente) {}
