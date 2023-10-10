package com.example.blogpessoal.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Obtém o cabeçalho de autorização da solicitação
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		try {
			// Verifica se o cabeçalho de autorização começa com "Bearer "
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				// Remove "Bearer " do cabeçalho para obter o token
				token = authHeader.substring(7);
				// Extrai o nome de usuário do token JWT
				username = jwtService.extractUsername(token);
			}

			// Verifica se o nome de usuário não é nulo e se não há autenticação no contexto
			// de segurança atual
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// Carrega os detalhes do usuário com base no nome de usuário
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				// Valida o token JWT
				if (jwtService.validateToken(token, userDetails)) {
					// Cria uma instância de autenticação e a define no contexto de segurança
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

			// Continua a cadeia de filtros
			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| ResponseStatusException e) {
			// Lida com exceções relacionadas ao token JWT
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}
	}

}
