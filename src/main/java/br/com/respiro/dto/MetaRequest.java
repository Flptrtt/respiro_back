package br.com.respiro.dto;

import jakarta.validation.constraints.*;

public record MetaRequest(@Min(1) @Max(12) int metaDiariaHoras) {}
