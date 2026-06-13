package com.agencia.viagens.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para criação e atualização de um Destino de Viagem.
 */
@Data
public class DestinoRequestDTO {

    @NotBlank(message = "O nome do destino é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "A localização do destino é obrigatória")
    @Size(max = 150, message = "A localização deve ter no máximo 150 caracteres")
    private String localizacao;

    @Size(max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
    private String descricao;

    @Size(max = 80, message = "O idioma deve ter no máximo 80 caracteres")
    private String idioma;

    @Size(max = 50, message = "A moeda deve ter no máximo 50 caracteres")
    private String moeda;
}
