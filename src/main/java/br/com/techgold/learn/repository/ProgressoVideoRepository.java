package br.com.techgold.learn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.techgold.learn.model.ProgressoVideo;

public interface ProgressoVideoRepository extends JpaRepository<ProgressoVideo, Long> {

	Optional<ProgressoVideo> findByFuncionarioIdAndVideoId(Long funcionarioId, Long videoId);

	@Query("SELECT p FROM ProgressoVideo p WHERE p.funcionario.id = :funcionarioId AND p.video.aula.curso.id = :cursoId")
	List<ProgressoVideo> buscarPorFuncionarioECurso(Long funcionarioId, Long cursoId);

	@Query("SELECT DISTINCT p.funcionario.id FROM ProgressoVideo p WHERE p.video.aula.curso.id = :cursoId")
	List<Long> buscarFuncionarioIdsPorCurso(Long cursoId);

	@Modifying
	@Query("DELETE FROM ProgressoVideo p WHERE p.funcionario.id = :funcionarioId AND p.video.aula.curso.id = :cursoId")
	void excluirPorFuncionarioECurso(Long funcionarioId, Long cursoId);

}
