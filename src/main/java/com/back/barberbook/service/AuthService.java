package com.back.barberbook.service;

import com.back.barberbook.dto.AuthDTO;
import com.back.barberbook.entity.Usuario;
import com.back.barberbook.repository.UsuarioRepository;
import com.back.barberbook.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private final UsuarioRepository repo;
	private final PasswordEncoder encoder;
	private final JwtService jwt;

	public AuthService(UsuarioRepository repo, PasswordEncoder encoder, JwtService jwt) {
		this.repo = repo;
		this.encoder = encoder;
		this.jwt = jwt;
	}

	public AuthDTO.LoginResponse authenticate(String email, String senha) {
		var user = repo.findByEmail(email.trim())
				.orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));
		if (!encoder.matches(senha, user.getSenhaHash())) {
			throw new IllegalArgumentException("Credenciais inválidas");
		}
		var token = jwt.generateToken(user.getId(), user.getEmail(), user.getRole().name());
		var view = new AuthDTO.UserView(user.getId(), user.getNome(), user.getEmail(), user.getTelefone(),
				user.getRole().name());
		return new AuthDTO.LoginResponse(token, view);
	}

	public AuthDTO.LoginResponse register(AuthDTO.RegisterRequest req) {
		if (req == null)
			throw new IllegalArgumentException("Dados obrigatórios não informados.");
		String nome = safe(req.nome());
		String email = lower(safe(req.email()));
		String telefone = safe(req.telefone());
		String cpfDigits = onlyDigits(safe(req.cpf()));
		String senha = safe(req.senha());

		if (nome.isBlank() || email.isBlank() || telefone.isBlank() || cpfDigits.isBlank() || senha.isBlank())
			throw new IllegalArgumentException("Preencha todos os campos.");
		if (cpfDigits.length() != 11 && cpfDigits.length() != 14)
			throw new IllegalArgumentException("CPF inválido.");

		if (repo.findByEmail(email).isPresent())
			throw new IllegalStateException("E-mail já cadastrado.");
		if (repo.findByCpf(cpfDigits.length() == 11 ? formatCpf(cpfDigits) : cpfDigits).isPresent())
			throw new IllegalStateException("CPF já cadastrado.");

		var u = new Usuario();
		u.setNome(nome);
		u.setEmail(email);
		u.setTelefone(telefone);
		u.setCpf(cpfDigits.length() == 11 ? formatCpf(cpfDigits) : cpfDigits);
		u.setSenhaHash(encoder.encode(senha));
		u.setRole(Usuario.Role.CLIENTE);

		var saved = repo.save(u);

		var token = jwt.generateToken(saved.getId(), saved.getEmail(), saved.getRole().name());
		var view = new AuthDTO.UserView(saved.getId(), saved.getNome(), saved.getEmail(), saved.getTelefone(),
				saved.getRole().name());
		return new AuthDTO.LoginResponse(token, view);
	}

	private static String safe(String s) {
		return s == null ? "" : s.trim();
	}

	private static String lower(String s) {
		return s.toLowerCase();
	}

	private static String onlyDigits(String s) {
		return s.replaceAll("\\D+", "");
	}

	private static String formatCpf(String d11) {
		return d11.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
	}
}
