package br.com.techgold.learn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgold.learn.model.CursoColaboradorAcesso;

public interface CursoColaboradorAcessoRepository extends JpaRepository<CursoColaboradorAcesso, Long> {

	boolean existsByCursoIdAndColaboradorId(Long cursoId, Long colaboradorId);

	List<CursoColaboradorAcesso> findByCursoId(Long cursoId);

	@Query("SELECT a.curso.id FROM CursoColaboradorAcesso a WHERE a.colaborador.id = :colaboradorId")
	List<Long> buscarCursoIdsPorColaboradorId(Long colaboradorId);

}
