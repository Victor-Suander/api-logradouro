package com.victors.apilogradouro.service;

import com.victors.apilogradouro.dto.request.UfRequestDTO;
import com.victors.apilogradouro.dto.response.CidadeResponseDTO;
import com.victors.apilogradouro.dto.response.UfResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UfService {

    UfResponseDTO salvar(UfRequestDTO dto);

    Page<UfResponseDTO> listar(Pageable pageable);

    UfResponseDTO buscarPorId(Long id);

    UfResponseDTO buscarPorSigla(String sigla);

    Page<CidadeResponseDTO> listarCidades(Long ufId, Pageable pageable);

    UfResponseDTO atualizar(Long id, UfRequestDTO dto);

    void deletar(Long id);
}
