package com.victors.apilogradouro.controller;

import com.victors.apilogradouro.dto.request.UfRequestDTO;
import com.victors.apilogradouro.dto.response.UfResponseDTO;
import com.victors.apilogradouro.service.UfService;
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

@Tag(name = "UF", description = "Operações relacionadas às Unidades Federativas")
@RestController
@RequestMapping("/ufs")
@RequiredArgsConstructor
public class UfController {

    private final UfService ufService;

    @Operation(summary = "Cadastrar nova UF")
    @PostMapping
    public ResponseEntity<UfResponseDTO> salvar(@Valid @RequestBody UfRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ufService.salvar(dto));
    }

    @Operation(summary = "Listar todas as UFs paginadas")
    @GetMapping
    public ResponseEntity<Page<UfResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ufService.listar(pageable));
    }

    @Operation(summary = "Buscar UF por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UfResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ufService.buscarPorId(id));
    }

    @Operation(summary = "Buscar UF por sigla")
    @GetMapping("/sigla/{sigla}")
    public ResponseEntity<UfResponseDTO> buscarPorSigla(@PathVariable String sigla) {
        return ResponseEntity.ok(ufService.buscarPorSigla(sigla));
    }

    @Operation(summary = "Atualizar UF por ID")
    @PutMapping("/{id}")
    public ResponseEntity<UfResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UfRequestDTO dto) {
        return ResponseEntity.ok(ufService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir UF por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ufService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
