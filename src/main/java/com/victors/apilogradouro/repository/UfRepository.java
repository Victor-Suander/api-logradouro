package com.victors.apilogradouro.repository;

import com.victors.apilogradouro.entity.Uf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// TODO: criar repository da Cidade seguindo o mesmo padrão

/**
 * Repositório JPA para a entidade Uf.
 * JpaRepository já fornece CRUD completo (save, findById, findAll, delete, etc.).
 * Os métodos abaixo são query methods — o Spring Data os implementa automaticamente
 * a partir do nome do método, sem necessidade de escrever SQL.
 */
@Repository
public interface UfRepository extends JpaRepository<Uf, Long> {

    /** Verifica se já existe uma UF com a sigla informada. Usado na validação ao criar uma nova UF. */
    boolean existsBySigla(String sigla);

    /**
     * Verifica se existe outra UF com a mesma sigla, excluindo o próprio registro pelo id.
     * Usado na validação ao atualizar uma UF: evita bloquear a edição do registro em si.
     */
    boolean existsBySiglaAndIdNot(String sigla, Long id);

    /**
     * Busca uma UF pela sigla.
     * Retorna Optional para forçar o tratamento explícito do caso em que a UF não é encontrada.
     */
    Optional<Uf> findBySigla(String sigla);
}