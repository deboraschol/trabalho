package com.agencia.viagens.exception;

/**
 * Lançada quando um Destino de Viagem não é encontrado pelo id informado.
 */
public class DestinoNotFoundException extends RuntimeException {

    public DestinoNotFoundException(String message) {
        super(message);
    }
}
