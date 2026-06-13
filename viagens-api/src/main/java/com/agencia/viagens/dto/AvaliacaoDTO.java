package com.agencia.viagens.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para avaliação de um Destino de Viagem.
 * Recebe uma nota de 1 a 10 e recalcula a média acumulada.
 */
@Data
public class AvaliacaoDTO {

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota mínima permitida é 1")
    @Max(value = 10, message = "A nota máxima permitida é 10")
    private Integer nota;
}
