package com.agencia.viagens.dto;
import com.agencia.viagens.model.UserRole;
public record RegistroDTO(String login, String senha, UserRole role) {}