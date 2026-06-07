package com.victors.apilogradouro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victors.apilogradouro.dto.response.CidadeResponseDTO;
import com.victors.apilogradouro.service.CidadeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CidadeController.class)
class CidadeValidacaoTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CidadeService cidadeService;

    @Test
    void deveRetornar400_quandoNomeVazio() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("nome", "", "ufId", 1));

        mockMvc.perform(post("/cidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoUfIdAusente() throws Exception {
        // ufId ausente — @NotNull deve rejeitar
        String body = objectMapper.writeValueAsString(Collections.singletonMap("nome", "São Paulo"));

        mockMvc.perform(post("/cidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar201_quandoDadosValidos() throws Exception {
        when(cidadeService.salvar(any())).thenReturn(new CidadeResponseDTO(1L, "São Paulo", 1L, "SP", null, null));

        String body = objectMapper.writeValueAsString(Map.of("nome", "São Paulo", "ufId", 1));

        mockMvc.perform(post("/cidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}
