package com.example.blogpessoal.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	// Chave secreta usada para assinar e verificar tokens JWT
	public static final String SECRET = "520d68139e503a8c4ebccf963f7951b05cfb7d15e73b101a3b1318beb231e57a";
	
	// Obtém a chave de assinatura a partir da chave secreta
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	// Extrai todas as reivindicações de um token JWT
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody();
	}
	
	// Extrai uma reivindicação específica de um token JWT usando uma função de resolução de reivindicações personalizada
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Extrai o nome de usuário de um token JWT
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	// Extrai a data de expiração de um token JWT
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	// Verifica se um token JWT está expirado
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	// Valida um token JWT com base em um UserDetails
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	// Cria um novo token JWT com base em reivindicações e um nome de usuário
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
					.setClaims(claims)
					.setSubject(userName)
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
					.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}
	
	// Gera um novo token JWT com base em um nome de usuário
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}

}
