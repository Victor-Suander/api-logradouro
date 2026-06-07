package com.victors.apilogradouro.controller;

import com.victors.apilogradouro.dto.response.UfResponseDTO;
import com.victors.apilogradouro.service.UfService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UfController.class)
class PaginacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UfService ufService;

    @Test
    void deveRetornarEstruturaDePaginacao() throws Exception {
        UfResponseDTO dto = new UfResponseDTO(1L, "SP", "São Paulo", null, null);
        when(ufService.listar(any())).thenReturn(
                new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/ufs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void deveRetornarPaginaCorreta_comParametrosPersonalizados() throws Exception {
        UfResponseDTO dto = new UfResponseDTO(1L, "SP", "São Paulo", null, null);
        // página 2 com size 5, total de 15 elementos
        when(ufService.listar(any())).thenReturn(
                new PageImpl<>(List.of(dto), PageRequest.of(2, 5), 15));

        mockMvc.perform(get("/ufs").param("page", "2").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(2))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.totalPages").value(3));
    }
}
