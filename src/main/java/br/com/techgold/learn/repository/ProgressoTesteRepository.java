package br.com.techgold.learn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.techgold.learn.model.ProgressoTeste;

public interface ProgressoTesteRepository extends JpaRepository<ProgressoTeste, Long> {

	Optional<ProgressoTeste> findByFuncionarioIdAndTesteId(Long funcionarioId, Long testeId);

	@Query("SELECT p FROM ProgressoTeste p WHERE p.funcionario.id = :funcionarioId AND p.teste.aula.curso.id = :cursoId")
	List<ProgressoTeste> buscarPorFuncionarioECurso(Long funcionarioId, Long cursoId);

	@Modifying
	@Query("DELETE FROM ProgressoTeste p WHERE p.funcionario.id = :funcionarioId AND p.teste.aula.curso.id = :cursoId")
	void excluirPorFuncionarioECurso(Long funcionarioId, Long cursoId);

}
