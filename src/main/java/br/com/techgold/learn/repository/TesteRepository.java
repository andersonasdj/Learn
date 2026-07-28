package br.com.techgold.learn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgold.learn.model.Teste;

public interface TesteRepository extends JpaRepository<Teste, Long> {

	Optional<Teste> findByAulaId(Long aulaId);

	boolean existsByAulaId(Long aulaId);

}
