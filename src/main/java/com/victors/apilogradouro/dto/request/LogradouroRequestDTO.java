package com.victors.apilogradouro.dto.request;

import com.victors.apilogradouro.entity.TipoLogradouro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LogradouroRequestDTO(

        @NotBlank
        @Size(min = 2, max = 150)
        String nome,

        @NotNull
        TipoLogradouro tipo,

        @NotBlank
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 99999-999 ou 99999999")
        String cep,

        @NotNull(message = "Bairro é obrigatório")
        Long bairroId
) {}
