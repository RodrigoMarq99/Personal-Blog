package com.example.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogpessoal.model.Postagens;
import com.example.blogpessoal.repository.PostagensRepository;

@RestController									   // Informar ao Spring que essa classe será um controlador da nossa API Rest
@RequestMapping("/postagens")					   // HTTP
@CrossOrigin(origins = "*", allowedHeaders = "*")  // Deixar que todas as origens de fora consigam acessar
public class PostagensController {

	@Autowired			// Injeção de dependência do Spring. Traz todos os métodos criados na classe Repository para cá. E como na Repository já possui o JPA, trazemos todos os métodos que ele nos dá por padrão
	private PostagensRepository postagensRepository;   // Injeção de dependência
	
	@GetMapping
	public ResponseEntity<List<Postagens>> getAll(){
		return ResponseEntity.ok(postagensRepository.findAll()); // Primeiro método de acesso. Será responsável por listar todas as postagens que terrmos no banco de dados no futuro
		
	}
	
													//Busca no banco de dados pelo ID
	@GetMapping("/{id}")
	public ResponseEntity<Postagens> getById(@PathVariable Long id){
		return postagensRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
}
	