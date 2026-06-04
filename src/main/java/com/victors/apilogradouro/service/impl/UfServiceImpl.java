package com.victors.apilogradouro.service.impl;

import com.victors.apilogradouro.dto.request.UfRequestDTO;
import com.victors.apilogradouro.dto.response.UfResponseDTO;
import com.victors.apilogradouro.entity.Uf;
import com.victors.apilogradouro.exception.MensagensErro;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.exception.RegraNegocioException;
import com.victors.apilogradouro.repository.UfRepository;
import com.victors.apilogradouro.service.UfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: criar testes unitários para este service

/**
 * @Service — registra esta classe como bean Spring gerenciado.
 * @RequiredArgsConstructor — Lombok gera construtor com todos os campos final,
 * permitindo injeção de dependência via construtor sem @Autowired.
 */
@Service
@RequiredArgsConstructor
public class UfServiceImpl implements UfService {

    private final UfRepository ufRepository;

    @Override
    public UfResponseDTO salvar(UfRequestDTO dto) {
        // Antes de persistir, verifica se já existe outra UF com a mesma sigla.
        // existsBySigla faz uma consulta COUNT otimizada, sem carregar a entidade inteira.
        if (ufRepository.existsBySigla(dto.sigla())) {
            throw new RegraNegocioException(MensagensErro.UF_SIGLA_DUPLICADA);
        }

        Uf uf = Uf.builder()
                .sigla(dto.sigla())
                .nome(dto.nome())
                .build();

        return toResponseDTO(ufRepository.save(uf));
    }

    @Override
    public List<UfResponseDTO> listar() {
        return ufRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public UfResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarOuLancarErro(id));
    }

    // TODO: implementar busca por sigla quando controller for criado
    @Override
    public UfResponseDTO buscarPorSigla(String sigla) {
        Uf uf = ufRepository.findBySigla(sigla)
                .orElseThrow(() -> new RecursoNaoEncontradoException(MensagensErro.UF_NAO_ENCONTRADA));
        return toResponseDTO(uf);
    }

    @Override
    public UfResponseDTO atualizar(Long id, UfRequestDTO dto) {
        Uf uf = buscarOuLancarErro(id);

        // Na atualização, usamos existsBySiglaAndIdNot para verificar duplicidade
        // excluindo o próprio registro da consulta. Sem isso, editar uma UF sem
        // mudar a sigla lançaria erro de duplicidade incorretamente.
        if (ufRepository.existsBySiglaAndIdNot(dto.sigla(), id)) {
            throw new RegraNegocioException(MensagensErro.UF_SIGLA_DUPLICADA);
        }

        uf.setSigla(dto.sigla());
        uf.setNome(dto.nome());

        return toResponseDTO(ufRepository.save(uf));
    }

    @Override
    public void deletar(Long id) {
        buscarOuLancarErro(id);
        ufRepository.deleteById(id);
    }

    /** Busca a UF pelo id ou lança RecursoNaoEncontradoException. Reutilizado em buscarPorId, atualizar e deletar. */
    private Uf buscarOuLancarErro(Long id) {
        return ufRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(MensagensErro.UF_NAO_ENCONTRADA));
    }

    /** Converte a entidade Uf para UfResponseDTO. Centraliza o mapeamento em um único lugar. */
    private UfResponseDTO toResponseDTO(Uf uf) {
        return new UfResponseDTO(uf.getId(), uf.getSigla(), uf.getNome());
    }
}
