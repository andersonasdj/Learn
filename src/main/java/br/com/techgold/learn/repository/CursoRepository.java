package br.com.techgold.learn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgold.learn.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

}
