package com.example.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.blogpessoal.model.Postagens;

public interface PostagensRepository extends JpaRepository<Postagens,Long>{
	// Para retornar todos os itens títulos que eu tenho. Ele fala o que tenho dentro de título
	public List<Postagens> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);
	
}
