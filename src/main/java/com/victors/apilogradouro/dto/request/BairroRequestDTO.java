package com.victors.apilogradouro.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BairroRequestDTO(

        @NotBlank
        @Size(min = 2, max = 120)
        String nome,

        @NotNull(message = "Cidade é obrigatória")
        Long cidadeId // referencia o id da Cidade pai
) {}
