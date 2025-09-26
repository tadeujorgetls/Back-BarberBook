package com.back.barberbook.controller;

import com.back.barberbook.dto.AuthDTO;
import com.back.barberbook.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService auth;

	public AuthController(AuthService auth) {
		this.auth = auth;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest req) {
		try {
			if (req == null || req.email() == null || req.senha() == null) {
				return ResponseEntity.badRequest().body("Informe email e senha.");
			}
			var resp = auth.authenticate(req.email().trim(), req.senha());
			return ResponseEntity.ok(resp);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody AuthDTO.RegisterRequest req) {
		try {
			var resp = auth.register(req);
			return ResponseEntity.status(HttpStatus.CREATED).body(resp);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
}
