package com.victors.apilogradouro.dto.response;

// TODO: adicionar campos createdAt e updatedAt quando auditoria for implementada

public record CidadeResponseDTO(
        Long id,
        String nome,
        Long ufId,
        String ufSigla
) {}
