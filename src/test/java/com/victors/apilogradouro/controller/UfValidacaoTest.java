package com.victors.apilogradouro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victors.apilogradouro.dto.response.UfResponseDTO;
import com.victors.apilogradouro.service.UfService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UfController.class)
class UfValidacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UfService ufService;

    @Test
    void deveRetornar400_quandoSiglaVazia() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("sigla", "", "nome", "São Paulo"));

        mockMvc.perform(post("/ufs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoSiglaMaiorQue2Caracteres() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("sigla", "SPA", "nome", "São Paulo"));

        mockMvc.perform(post("/ufs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400_quandoNomeVazio() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("sigla", "SP", "nome", ""));

        mockMvc.perform(post("/ufs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar201_quandoDadosValidos() throws Exception {
        when(ufService.salvar(any())).thenReturn(new UfResponseDTO(1L, "SP", "São Paulo", null, null));

        String body = objectMapper.writeValueAsString(Map.of("sigla", "SP", "nome", "São Paulo"));

        mockMvc.perform(post("/ufs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}
