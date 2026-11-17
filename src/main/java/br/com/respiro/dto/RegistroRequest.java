package br.com.respiro.dto;

import jakarta.validation.constraints.*;

public record RegistroRequest(
        @NotBlank(message = "Informe seu nome") @Size(max = 60) String nome,
        @NotBlank(message = "Informe seu e-mail") @Email(message = "E-mail inválido") String email,
        @NotBlank @Size(min = 6, message = "A senha precisa ter ao menos 6 caracteres") String senha) {}
