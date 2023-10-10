package com.example.blogpessoal.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.example.blogpessoal.model.Postagem;
import com.example.blogpessoal.repository.PostagemRepository;
import com.example.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController									   // Informar ao Spring que essa classe será um controlador da nossa API Rest
@RequestMapping("/postagem")					   // HTTP
@CrossOrigin(origins = "*", allowedHeaders = "*")  // Deixar que todas as origens de fora consigam acessar
public class PostagemController {

	@Autowired			// Injeção de dependência do Spring. Traz todos os métodos criados na classe Repository para cá. E como na Repository já possui o JPA, trazemos todos os métodos que ele nos dá por padrão
	private PostagemRepository postagemRepository;   // Injeção de dependência
	
	@Autowired
	private TemaRepository temaRepository;
	
	
	@GetMapping
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(postagemRepository.findAll()); // Primeiro método de acesso. Será responsável por listar todas as postagens que terrmos no banco de dados no futuro
		
	}
	
													//Busca no banco de dados pelo ID
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id){
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
		if (temaRepository.existsById(postagem.getTema().getId())) {
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(postagemRepository.save(postagem));
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
	}
	
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		if (postagemRepository.existsById(postagem.getId())) {
			if (temaRepository.existsById(postagem.getTema().getId())) {
				return ResponseEntity.status(HttpStatus.OK)
					.body(postagemRepository.save(postagem));
			} // verificar chaves do if
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Postagem> postagem = postagemRepository.findById(id);
		
		if(postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		postagemRepository.deleteById(id);
	}
	
}