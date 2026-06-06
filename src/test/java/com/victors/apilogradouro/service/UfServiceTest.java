package com.victors.apilogradouro.service;

import com.victors.apilogradouro.dto.request.UfRequestDTO;
import com.victors.apilogradouro.dto.response.UfResponseDTO;
import com.victors.apilogradouro.entity.Uf;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.exception.RegraNegocioException;
import com.victors.apilogradouro.repository.CidadeRepository;
import com.victors.apilogradouro.repository.UfRepository;
import com.victors.apilogradouro.service.impl.UfServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UfServiceTest {

    @Mock
    private UfRepository ufRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @InjectMocks
    private UfServiceImpl ufService;

    @Test
    void salvar_comSiglaDuplicada_lancaRegraNegocioException() {
        when(ufRepository.existsBySigla("SP")).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> ufService.salvar(new UfRequestDTO("SP", "São Paulo")));
    }

    @Test
    void salvar_comSiglaNova_salvaSucesso() {
        Uf uf = Uf.builder().id(1L).sigla("SP").nome("São Paulo").build();
        when(ufRepository.existsBySigla("SP")).thenReturn(false);
        when(ufRepository.save(any())).thenReturn(uf);

        UfResponseDTO result = ufService.salvar(new UfRequestDTO("SP", "São Paulo"));

        assertThat(result.sigla()).isEqualTo("SP");
        assertThat(result.nome()).isEqualTo("São Paulo");
    }

    @Test
    void buscarPorId_comIdInexistente_lancaRecursoNaoEncontradoException() {
        when(ufRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> ufService.buscarPorId(99L));
    }

    @Test
    void atualizar_comSiglaDuplicadaDeOutraUf_lancaRegraNegocioException() {
        Uf uf = Uf.builder().id(1L).sigla("SP").nome("São Paulo").build();
        when(ufRepository.findById(1L)).thenReturn(Optional.of(uf));
        when(ufRepository.existsBySiglaAndIdNot("RJ", 1L)).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> ufService.atualizar(1L, new UfRequestDTO("RJ", "Rio de Janeiro")));
    }

    @Test
    void deletar_comCidadesVinculadas_lancaRegraNegocioException() {
        Uf uf = Uf.builder().id(1L).sigla("SP").nome("São Paulo").build();
        when(ufRepository.findById(1L)).thenReturn(Optional.of(uf));
        when(cidadeRepository.existsByUfId(1L)).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> ufService.deletar(1L));
    }
}
