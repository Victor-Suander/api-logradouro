package com.victors.apilogradouro.dto.response;

import java.time.LocalDateTime;

public record CidadeResponseDTO(
        Long id,
        String nome,
        Long ufId,
        String ufSigla,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
