package com.victors.apilogradouro.service.impl;

import com.victors.apilogradouro.dto.request.CidadeRequestDTO;
import com.victors.apilogradouro.dto.response.BairroResponseDTO;
import com.victors.apilogradouro.dto.response.CidadeResponseDTO;
import com.victors.apilogradouro.entity.Cidade;
import com.victors.apilogradouro.entity.Uf;
import com.victors.apilogradouro.exception.MensagensErro;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.exception.RegraNegocioException;
import com.victors.apilogradouro.repository.BairroRepository;
import com.victors.apilogradouro.repository.CidadeRepository;
import com.victors.apilogradouro.repository.UfRepository;
import com.victors.apilogradouro.service.CidadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// TODO: criar testes unitários para este service

@Slf4j
@Service
@RequiredArgsConstructor
public class CidadeServiceImpl implements CidadeService {

    private final CidadeRepository cidadeRepository;
    private final UfRepository ufRepository;
    private final BairroRepository bairroRepository;

    @Override
    public CidadeResponseDTO salvar(CidadeRequestDTO dto) {
        log.info("Salvando Cidade: nome={}", dto.nome());
        if (cidadeRepository.existsByNome(dto.nome())) {
            log.warn("Nome de Cidade duplicado: {}", dto.nome());
            throw new RegraNegocioException(MensagensErro.CIDADE_NOME_DUPLICADO);
        }

        Uf uf = buscarUfOuLancarErro(dto.ufId());

        Cidade cidade = Cidade.builder()
                .nome(dto.nome())
                .uf(uf)
                .build();

        CidadeResponseDTO result = toResponseDTO(cidadeRepository.save(cidade));
        log.info("Cidade salva com sucesso: id={}", result.id());
        return result;
    }

    @Override
    public Page<CidadeResponseDTO> listar(Pageable pageable) {
        log.debug("Listando Cidades: pageable={}", pageable);
        return cidadeRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Override
    public CidadeResponseDTO buscarPorId(Long id) {
        log.debug("Buscando Cidade por id: {}", id);
        return toResponseDTO(buscarOuLancarErro(id));
    }

    @Override
    public CidadeResponseDTO atualizar(Long id, CidadeRequestDTO dto) {
        log.info("Atualizando Cidade: id={}", id);
        Cidade cidade = buscarOuLancarErro(id);

        // exclui o próprio id da consulta — sem isso, editar sem mudar o nome lançaria falso erro de duplicidade
        if (cidadeRepository.existsByNomeAndIdNot(dto.nome(), id)) {
            log.warn("Nome de Cidade duplicado na atualização: {}", dto.nome());
            throw new RegraNegocioException(MensagensErro.CIDADE_NOME_DUPLICADO);
        }

        cidade.setNome(dto.nome());
        cidade.setUf(buscarUfOuLancarErro(dto.ufId()));

        CidadeResponseDTO result = toResponseDTO(cidadeRepository.save(cidade));
        log.info("Cidade atualizada com sucesso: id={}", id);
        return result;
    }

    @Override
    public void deletar(Long id) {
        log.warn("Deletando Cidade: id={}", id);
        buscarOuLancarErro(id);
        // impede exclusão de cidade com bairros vinculados — evitaria violação de FK no banco
        if (bairroRepository.existsByCidadeId(id)) {
            log.warn("Tentativa de excluir Cidade com bairros vinculados: id={}", id);
            throw new RegraNegocioException(MensagensErro.CIDADE_COM_BAIRROS);
        }
        cidadeRepository.deleteById(id);
        log.info("Cidade deletada com sucesso: id={}", id);
    }

    @Override
    public Page<BairroResponseDTO> listarBairros(Long cidadeId, Pageable pageable) {
        buscarOuLancarErro(cidadeId);
        return bairroRepository.findByCidadeId(cidadeId, pageable)
                .map(b -> new BairroResponseDTO(
                        b.getId(), b.getNome(),
                        b.getCidade().getId(), b.getCidade().getNome(),
                        b.getCreatedAt(), b.getUpdatedAt()));
    }

    private Cidade buscarOuLancarErro(Long id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cidade não encontrada: id={}", id);
                    return new RecursoNaoEncontradoException(MensagensErro.CIDADE_NAO_ENCONTRADA);
                });
    }

    private Uf buscarUfOuLancarErro(Long ufId) {
        return ufRepository.findById(ufId)
                .orElseThrow(() -> {
                    log.warn("UF não encontrada ao buscar para Cidade: id={}", ufId);
                    return new RecursoNaoEncontradoException(MensagensErro.UF_NAO_ENCONTRADA);
                });
    }

    private CidadeResponseDTO toResponseDTO(Cidade cidade) {
        return new CidadeResponseDTO(
                cidade.getId(),
                cidade.getNome(),
                cidade.getUf().getId(),
                cidade.getUf().getSigla(),
                cidade.getCreatedAt(),
                cidade.getUpdatedAt()
        );
    }
}
