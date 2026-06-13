package com.agencia.viagens.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa um Destino de Viagem.
 */
@Entity
@Table(name = "destinos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome do destino (ex.: "Paris", "Machu Picchu"). */
    @Column(nullable = false, length = 150)
    private String nome;

    /** Localização geográfica (país, cidade ou região). */
    @Column(nullable = false, length = 150)
    private String localizacao;

    /** Descrição completa do destino. */
    @Column(columnDefinition = "TEXT")
    private String descricao;

    /** Idioma principal falado no destino. */
    @Column(length = 80)
    private String idioma;

    /** Moeda local utilizada no destino. */
    @Column(length = 50)
    private String moeda;

    /** Nota média calculada com base nas avaliações recebidas. */
    @Column(name = "nota_media")
    private Double notaMedia;

    /** Quantidade total de avaliações recebidas. */
    @Column(name = "total_avaliacoes")
    private Integer totalAvaliacoes;
}
