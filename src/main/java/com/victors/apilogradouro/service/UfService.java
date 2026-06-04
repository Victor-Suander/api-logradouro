package com.victors.apilogradouro.service;

import com.victors.apilogradouro.dto.request.UfRequestDTO;
import com.victors.apilogradouro.dto.response.UfResponseDTO;

import java.util.List;

public interface UfService {

    UfResponseDTO salvar(UfRequestDTO dto);

    List<UfResponseDTO> listar();

    UfResponseDTO buscarPorId(Long id);

    UfResponseDTO buscarPorSigla(String sigla);

    UfResponseDTO atualizar(Long id, UfRequestDTO dto);

    void deletar(Long id);
}
