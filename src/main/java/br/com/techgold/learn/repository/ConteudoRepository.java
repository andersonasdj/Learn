package br.com.techgold.learn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgold.learn.model.Conteudo;

public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {

	List<Conteudo> findByAulaIdOrderByOrdemAsc(Long aulaId);

}
