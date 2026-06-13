package com.agencia.viagens.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de saída com as informações completas de um Destino de Viagem.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinoResponseDTO {

    private Long id;
    private String nome;
    private String localizacao;
    private String descricao;
    private String idioma;
    private String moeda;

    /** Nota média atual do destino (0.0 a 10.0). */
    private Double notaMedia;

    /** Número total de avaliações recebidas. */
    private Integer totalAvaliacoes;
}
