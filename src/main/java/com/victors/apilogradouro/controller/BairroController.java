package com.victors.apilogradouro.controller;

import com.victors.apilogradouro.dto.request.BairroRequestDTO;
import com.victors.apilogradouro.dto.response.BairroResponseDTO;
import com.victors.apilogradouro.service.BairroService;
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

@Tag(name = "Bairro", description = "Operações relacionadas aos Bairros")
@RestController
@RequestMapping("/bairros")
@RequiredArgsConstructor
public class BairroController {

    private final BairroService bairroService;

    @Operation(summary = "Cadastrar novo bairro")
    @PostMapping
    public ResponseEntity<BairroResponseDTO> salvar(@Valid @RequestBody BairroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bairroService.salvar(dto));
    }

    @Operation(summary = "Listar todos os bairros paginados")
    @GetMapping
    public ResponseEntity<Page<BairroResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(bairroService.listar(pageable));
    }

    @Operation(summary = "Buscar bairro por ID")
    @GetMapping("/{id}")
    public ResponseEntity<BairroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bairroService.buscarPorId(id));
    }

    // endpoint hierárquico — retorna os logradouros pertencentes a um bairro específico
    @Operation(summary = "Listar logradouros de um bairro")
    @GetMapping("/{id}/logradouros")
    public ResponseEntity<Page<Object>> listarLogradouros(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(bairroService.listarLogradouros(id, pageable));
    }

    @Operation(summary = "Atualizar bairro por ID")
    @PutMapping("/{id}")
    public ResponseEntity<BairroResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody BairroRequestDTO dto) {
        return ResponseEntity.ok(bairroService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir bairro por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        bairroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
