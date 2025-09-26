package com.back.barberbook.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final Key key;
  private final long expMinutes;

  public JwtService(
      @Value("${security.jwt.secret:dev-super-secret-please-change}") String secret,
      @Value("${security.jwt.exp-minutes:120}") long expMinutes
  ) {
    this.key = deriveKey(secret);
    this.expMinutes = expMinutes;
  }

  private Key deriveKey(String secret) {
    try {
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      byte[] hash = sha.digest(secret.getBytes(StandardCharsets.UTF_8));
      return Keys.hmacShaKeyFor(hash);
    } catch (Exception e) {
      byte[] raw = secret.getBytes(StandardCharsets.UTF_8);
      return Keys.hmacShaKeyFor(raw.length >= 32 ? raw : padTo32(raw));
    }
  }

  private byte[] padTo32(byte[] in) {
    byte[] out = new byte[32];
    for (int i = 0; i < out.length; i++) {
      out[i] = i < in.length ? in[i] : (byte) (i * 31 + 7);
    }
    return out;
  }

  public String generateToken(UUID userId, String email, String role) {
    Instant now = Instant.now();
    return Jwts.builder()
        .setSubject(userId.toString())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(expMinutes, ChronoUnit.MINUTES)))
        .addClaims(Map.of(
            "email", email,
            "role", role
        ))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }
}