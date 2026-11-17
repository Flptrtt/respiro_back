package br.com.respiro.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record OnboardingRequest(
        @Min(0) @Max(24) int horasIniciais,
        @Min(1) @Max(12) int metaDiariaHoras,
        @NotEmpty(message = "Escolha ao menos um interesse") @Size(max = 8) List<String> interesses) {}
