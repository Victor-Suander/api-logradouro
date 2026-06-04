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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// TODO: criar testes unitários para este service

// @RequiredArgsConstructor gera construtor com campos final — injeção via construtor sem @Autowired
@Service
@RequiredArgsConstructor
public class UfServiceImpl implements UfService {

    private final UfRepository ufRepository;

    @Override
    public UfResponseDTO salvar(UfRequestDTO dto) {
        // existsBySigla faz COUNT otimizado, sem carregar a entidade inteira
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
    public Page<UfResponseDTO> listar(Pageable pageable) {
        return ufRepository.findAll(pageable).map(this::toResponseDTO);
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

        // exclui o próprio id da consulta — sem isso, editar sem mudar a sigla lançaria falso erro de duplicidade
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

    private Uf buscarOuLancarErro(Long id) {
        return ufRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(MensagensErro.UF_NAO_ENCONTRADA));
    }

    private UfResponseDTO toResponseDTO(Uf uf) {
        return new UfResponseDTO(uf.getId(), uf.getSigla(), uf.getNome());
    }
}
