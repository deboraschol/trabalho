package com.agencia.viagens.repository;

import com.agencia.viagens.model.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para a entidade {@link Destino}.
 */
@Repository
public interface DestinoRepository extends JpaRepository<Destino, Long> {

    /**
     * Pesquisa destinos por nome e/ou localização (ambos case-insensitive e parciais).
     * Ambos os parâmetros são opcionais: se nulos, a condição correspondente é ignorada.
     *
     * @param nome        parte do nome a buscar (ou {@code null} para ignorar)
     * @param localizacao parte da localização a buscar (ou {@code null} para ignorar)
     * @return lista de destinos que satisfazem os critérios
     */
    @Query("""
            SELECT d FROM Destino d
            WHERE (:nome IS NULL
                   OR LOWER(d.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
              AND (:localizacao IS NULL
                   OR LOWER(d.localizacao) LIKE LOWER(CONCAT('%', :localizacao, '%')))
            ORDER BY d.nome
            """)
    List<Destino> pesquisarPorNomeOuLocalizacao(
            @Param("nome") String nome,
            @Param("localizacao") String localizacao
    );
}
