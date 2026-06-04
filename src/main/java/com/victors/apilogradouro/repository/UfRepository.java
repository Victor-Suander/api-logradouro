package com.victors.apilogradouro.repository;

import com.victors.apilogradouro.entity.Uf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// TODO: criar repository da Cidade seguindo o mesmo padrão
// Spring Data implementa os métodos abaixo automaticamente a partir do nome, sem SQL

@Repository
public interface UfRepository extends JpaRepository<Uf, Long> {

    // usado na validação de criação
    boolean existsBySigla(String sigla);

    // exclui o próprio registro da consulta — evita falso positivo ao editar sem mudar a sigla
    boolean existsBySiglaAndIdNot(String sigla, Long id);

    // Optional força tratamento explícito do caso não encontrado
    Optional<Uf> findBySigla(String sigla);
}
