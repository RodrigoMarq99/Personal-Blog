package com.example.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity  						// Vai transformar minha classe em uma tabela para o banco de dados
@Table(name = "tb_postagens")   // Criar tabela
public class Postagens {
	
	// Notações precisam estar grudadas com o argumento
	@Id                         						// O Id configura como chave primária
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Incremento
	private Long id; 									// Long é a referência ao bigint do banco de dados
	
	
	@NotBlank(message = "Atributo obrigatório")    		// Não pode ficar vazio
	@Size(min = 5, max = 255)                           // Tamanho
	private String titulo;
	
	@NotBlank(message = "Atributo obrigatório")    		// Não pode ficar vazio
	@Size(min = 10, max = 1000)                         // Tamanho
	private String texto;
	
	@UpdateTimestamp									// Preenchida automaticamente com data e hora do sistema
	private LocalDateTime data; 						// Configuração para pegar data e hora

	@ManyToOne
	@JsonIgnoreProperties("postagem")                   // Não dar loop infinito
	private Tema tema;
									
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}
	
	
	
}
