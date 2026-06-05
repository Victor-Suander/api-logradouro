package com.victors.apilogradouro.controller;

import com.victors.apilogradouro.dto.request.CidadeRequestDTO;
import com.victors.apilogradouro.dto.response.CidadeResponseDTO;
import com.victors.apilogradouro.service.CidadeService;
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

@Tag(name = "Cidade", description = "Operações relacionadas às Cidades")
@RestController
@RequestMapping("/cidades")
@RequiredArgsConstructor
public class CidadeController {

    private final CidadeService cidadeService;

    @Operation(summary = "Cadastrar nova cidade")
    @PostMapping
    public ResponseEntity<CidadeResponseDTO> salvar(@Valid @RequestBody CidadeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cidadeService.salvar(dto));
    }

    @Operation(summary = "Listar todas as cidades paginadas")
    @GetMapping
    public ResponseEntity<Page<CidadeResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(cidadeService.listar(pageable));
    }

    @Operation(summary = "Buscar cidade por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cidadeService.buscarPorId(id));
    }

    // endpoint hierárquico — retorna os bairros pertencentes a uma cidade específica
    @Operation(summary = "Listar bairros de uma cidade")
    @GetMapping("/{id}/bairros")
    public ResponseEntity<Page<Object>> listarBairros(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(cidadeService.listarBairros(id, pageable));
    }

    @Operation(summary = "Atualizar cidade por ID")
    @PutMapping("/{id}")
    public ResponseEntity<CidadeResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CidadeRequestDTO dto) {
        return ResponseEntity.ok(cidadeService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir cidade por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
