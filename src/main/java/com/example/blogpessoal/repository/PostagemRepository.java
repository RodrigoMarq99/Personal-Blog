package com.example.blogpessoal.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.example.blogpessoal.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem,Long>{
	// Para retornar todos os itens títulos que eu tenho. Ele fala o que tenho dentro de título
	public List<Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);
	
}
