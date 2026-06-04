package com.victors.apilogradouro.dto.response;

// TODO: adicionar campos createdAt e updatedAt quando auditoria for implementada

/**
 * DTO de saída para UF. Expõe apenas os dados necessários para o cliente,
 * sem vazar detalhes internos da entidade (ex: versão, metadados JPA).
 */
public record UfResponseDTO(
        Long id,
        String sigla,
        String nome
) {}
