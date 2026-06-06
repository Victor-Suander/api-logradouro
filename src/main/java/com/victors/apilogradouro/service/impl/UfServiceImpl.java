package com.victors.apilogradouro.service.impl;

import com.victors.apilogradouro.dto.request.UfRequestDTO;
import com.victors.apilogradouro.dto.response.CidadeResponseDTO;
import com.victors.apilogradouro.dto.response.UfResponseDTO;
import com.victors.apilogradouro.entity.Uf;
import com.victors.apilogradouro.exception.MensagensErro;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.exception.RegraNegocioException;
import com.victors.apilogradouro.repository.CidadeRepository;
import com.victors.apilogradouro.repository.UfRepository;
import com.victors.apilogradouro.service.UfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// TODO: criar testes unitários para este service

// @RequiredArgsConstructor gera construtor com campos final — injeção via construtor sem @Autowired
@Slf4j
@Service
@RequiredArgsConstructor
public class UfServiceImpl implements UfService {

    private final UfRepository ufRepository;
    private final CidadeRepository cidadeRepository;

    @Override
    public UfResponseDTO salvar(UfRequestDTO dto) {
        log.info("Salvando UF: sigla={}", dto.sigla());
        // existsBySigla faz COUNT otimizado, sem carregar a entidade inteira
        if (ufRepository.existsBySigla(dto.sigla())) {
            log.warn("Sigla de UF duplicada: {}", dto.sigla());
            throw new RegraNegocioException(MensagensErro.UF_SIGLA_DUPLICADA);
        }

        Uf uf = Uf.builder()
                .sigla(dto.sigla())
                .nome(dto.nome())
                .build();

        UfResponseDTO result = toResponseDTO(ufRepository.save(uf));
        log.info("UF salva com sucesso: id={}", result.id());
        return result;
    }

    @Override
    public Page<UfResponseDTO> listar(Pageable pageable) {
        log.debug("Listando UFs: pageable={}", pageable);
        return ufRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Override
    public UfResponseDTO buscarPorId(Long id) {
        log.debug("Buscando UF por id: {}", id);
        return toResponseDTO(buscarOuLancarErro(id));
    }

    @Override
    public Page<CidadeResponseDTO> listarCidades(Long ufId, Pageable pageable) {
        buscarOuLancarErro(ufId);
        return cidadeRepository.findByUfId(ufId, pageable)
                .map(c -> new CidadeResponseDTO(
                        c.getId(), c.getNome(),
                        c.getUf().getId(), c.getUf().getSigla(),
                        c.getCreatedAt(), c.getUpdatedAt()));
    }

    @Override
    public UfResponseDTO buscarPorSigla(String sigla) {
        Uf uf = ufRepository.findBySigla(sigla)
                .orElseThrow(() -> {
                    log.warn("UF não encontrada: sigla={}", sigla);
                    return new RecursoNaoEncontradoException(MensagensErro.UF_NAO_ENCONTRADA);
                });
        return toResponseDTO(uf);
    }

    @Override
    public UfResponseDTO atualizar(Long id, UfRequestDTO dto) {
        log.info("Atualizando UF: id={}", id);
        Uf uf = buscarOuLancarErro(id);

        // exclui o próprio id da consulta — sem isso, editar sem mudar a sigla lançaria falso erro de duplicidade
        if (ufRepository.existsBySiglaAndIdNot(dto.sigla(), id)) {
            log.warn("Sigla de UF duplicada na atualização: {}", dto.sigla());
            throw new RegraNegocioException(MensagensErro.UF_SIGLA_DUPLICADA);
        }

        uf.setSigla(dto.sigla());
        uf.setNome(dto.nome());

        UfResponseDTO result = toResponseDTO(ufRepository.save(uf));
        log.info("UF atualizada com sucesso: id={}", id);
        return result;
    }

    @Override
    public void deletar(Long id) {
        log.warn("Deletando UF: id={}", id);
        buscarOuLancarErro(id);
        // impede exclusão de UF com cidades vinculadas — evitaria violação de FK no banco
        if (cidadeRepository.existsByUfId(id)) {
            log.warn("Tentativa de excluir UF com cidades vinculadas: id={}", id);
            throw new RegraNegocioException(MensagensErro.UF_COM_CIDADES);
        }
        ufRepository.deleteById(id);
        log.info("UF deletada com sucesso: id={}", id);
    }

    private Uf buscarOuLancarErro(Long id) {
        return ufRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("UF não encontrada: id={}", id);
                    return new RecursoNaoEncontradoException(MensagensErro.UF_NAO_ENCONTRADA);
                });
    }

    private UfResponseDTO toResponseDTO(Uf uf) {
        return new UfResponseDTO(uf.getId(), uf.getSigla(), uf.getNome(), uf.getCreatedAt(), uf.getUpdatedAt());
    }
}
