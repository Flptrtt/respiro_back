package br.com.respiro.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record InteressesRequest(@NotEmpty(message = "Escolha ao menos um interesse") @Size(max = 8) List<String> interesses) {}
