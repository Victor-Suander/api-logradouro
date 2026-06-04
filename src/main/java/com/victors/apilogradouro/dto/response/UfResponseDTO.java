package com.victors.apilogradouro.dto.response;

// TODO: adicionar campos createdAt e updatedAt quando auditoria for implementada

public record UfResponseDTO(
        Long id,
        String sigla,
        String nome
) {}
