package com.victors.apilogradouro.controller;

import com.victors.apilogradouro.dto.response.BairroResponseDTO;
import com.victors.apilogradouro.dto.response.CidadeResponseDTO;
import com.victors.apilogradouro.dto.response.LogradouroResponseDTO;
import com.victors.apilogradouro.dto.response.UfResponseDTO;
import com.victors.apilogradouro.entity.TipoLogradouro;
import com.victors.apilogradouro.exception.RecursoNaoEncontradoException;
import com.victors.apilogradouro.service.BairroService;
import com.victors.apilogradouro.service.CidadeService;
import com.victors.apilogradouro.service.UfService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UfController.class, CidadeController.class, BairroController.class})
class HierarquiaEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UfService ufService;

    @MockitoBean
    private CidadeService cidadeService;

    @MockitoBean
    private BairroService bairroService;

    @Test
    void buscarPorSigla_comSiglaMaiuscula_retorna200ComUfCorreta() throws Exception {
        when(ufService.buscarPorSigla("SP"))
                .thenReturn(new UfResponseDTO(1L, "SP", "São Paulo", null, null));

        mockMvc.perform(get("/ufs/sigla/SP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sigla").value("SP"))
                .andExpect(jsonPath("$.nome").value("São Paulo"));
    }

    @Test
    void buscarPorSigla_comSiglaMinuscula_retorna404() throws Exception {
        // busca é case-sensitive — "sp" não corresponde a "SP" cadastrado
        when(ufService.buscarPorSigla("sp"))
                .thenThrow(new RecursoNaoEncontradoException("UF não encontrada."));

        mockMvc.perform(get("/ufs/sigla/sp"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarCidades_comUfExistente_retorna200ComListaPaginada() throws Exception {
        CidadeResponseDTO cidade = new CidadeResponseDTO(1L, "São Paulo", 1L, "SP", null, null);
        Page<CidadeResponseDTO> page = new PageImpl<>(List.of(cidade), PageRequest.of(0, 10), 1);
        when(ufService.listarCidades(eq(1L), any())).thenReturn(page);

        mockMvc.perform(get("/ufs/1/cidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].nome").value("São Paulo"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void listarCidades_comUfInexistente_retornaListaVazia() throws Exception {
        when(ufService.listarCidades(eq(99L), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/ufs/99/cidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void listarBairros_comCidadeExistente_retorna200ComListaPaginada() throws Exception {
        BairroResponseDTO bairro = new BairroResponseDTO(1L, "Centro", 1L, "São Paulo", null, null);
        Page<BairroResponseDTO> page = new PageImpl<>(List.of(bairro), PageRequest.of(0, 10), 1);
        when(cidadeService.listarBairros(eq(1L), any())).thenReturn(page);

        mockMvc.perform(get("/cidades/1/bairros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].nome").value("Centro"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void listarLogradouros_comBairroExistente_retorna200ComListaPaginada() throws Exception {
        LogradouroResponseDTO logradouro = new LogradouroResponseDTO(
                1L, "Avenida Paulista", TipoLogradouro.AVENIDA, "01310-100",
                1L, "Bela Vista", "São Paulo", "SP", null, null);
        Page<LogradouroResponseDTO> page = new PageImpl<>(List.of(logradouro), PageRequest.of(0, 10), 1);
        when(bairroService.listarLogradouros(eq(1L), any())).thenReturn(page);

        mockMvc.perform(get("/bairros/1/logradouros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].cep").value("01310-100"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
