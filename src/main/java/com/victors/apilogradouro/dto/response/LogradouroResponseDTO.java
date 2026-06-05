package com.victors.apilogradouro.dto.response;

import com.victors.apilogradouro.entity.TipoLogradouro;

import java.time.LocalDateTime;

public record LogradouroResponseDTO(
        Long id,
        String nome,
        TipoLogradouro tipo,
        String cep,
        Long bairroId,
        String bairroNome,
        String cidadeNome,
        String ufSigla,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
