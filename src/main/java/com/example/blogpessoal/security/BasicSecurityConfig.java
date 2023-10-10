package com.example.blogpessoal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    // Configuração para fornecer uma instância de UserDetailsService
    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // Configuração para fornecer um codificador de senhas
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuração para fornecer um provedor de autenticação
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        
        // Configura o serviço de detalhes do usuário e o codificador de senhas
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // Configuração para fornecer um gerenciador de autenticação
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configuração para definir as regras de segurança HTTP
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configura o gerenciamento de sessões e desabilita a proteção CSRF
        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().csrf().disable()
            .cors();

        http
            .authorizeHttpRequests((auth) -> auth
                // Permite o acesso público a determinados endpoints
                .requestMatchers("/usuarios/logar").permitAll()
                .requestMatchers("/usuarios/cadastrar").permitAll()
                .requestMatchers("/error/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                // Exige autenticação para todas as outras solicitações
                .anyRequest().authenticated())
            .authenticationProvider(authenticationProvider())
            // Adiciona o filtro JWT antes do filtro padrão de autenticação de nome de usuário e senha
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic();

        return http.build();
    }
}