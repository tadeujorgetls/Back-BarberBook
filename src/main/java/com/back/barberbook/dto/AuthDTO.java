package com.back.barberbook.dto;

import java.util.UUID;

public class AuthDTO {
	public record LoginRequest(String email, String senha) {
	}

	public record UserView(UUID id, String nome, String email, String telefone, String role) {
	}

	public record LoginResponse(String token, UserView user) {
	}

	// REGISTRO
	public record RegisterRequest(String nome, String cpf, String email, String telefone, String senha) {
	}
}
