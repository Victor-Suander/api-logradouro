package com.victors.apilogradouro.controller;

import com.victors.apilogradouro.dto.request.LogradouroRequestDTO;
import com.victors.apilogradouro.dto.response.LogradouroResponseDTO;
import com.victors.apilogradouro.service.LogradouroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Logradouro", description = "Operações relacionadas aos Logradouros")
@RestController
@RequestMapping("/logradouros")
@RequiredArgsConstructor
public class LogradouroController {

    private final LogradouroService logradouroService;

    @Operation(summary = "Cadastrar novo logradouro")
    @PostMapping
    public ResponseEntity<LogradouroResponseDTO> salvar(@Valid @RequestBody LogradouroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logradouroService.salvar(dto));
    }

    @Operation(summary = "Listar todos os logradouros paginados")
    @GetMapping
    public ResponseEntity<Page<LogradouroResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(logradouroService.listar(pageable));
    }

    @Operation(summary = "Buscar logradouro por ID")
    @GetMapping("/{id}")
    public ResponseEntity<LogradouroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(logradouroService.buscarPorId(id));
    }

    @Operation(summary = "Buscar logradouro por CEP")
    @GetMapping("/cep/{cep}")
    public ResponseEntity<LogradouroResponseDTO> buscarPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(logradouroService.buscarPorCep(cep));
    }

    @Operation(summary = "Atualizar logradouro por ID")
    @PutMapping("/{id}")
    public ResponseEntity<LogradouroResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LogradouroRequestDTO dto) {
        return ResponseEntity.ok(logradouroService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir logradouro por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        logradouroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
