package br.com.techgold.learn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgold.learn.model.Aula;

public interface AulaRepository extends JpaRepository<Aula, Long> {

	List<Aula> findByCursoIdOrderByOrdemAsc(Long cursoId);

}
