package br.com.techgold.learn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgold.learn.model.CursoClienteAcesso;

public interface CursoClienteAcessoRepository extends JpaRepository<CursoClienteAcesso, Long> {

	boolean existsByCursoIdAndClienteId(Long cursoId, Long clienteId);

	List<CursoClienteAcesso> findByCursoId(Long cursoId);

	@Query("SELECT a.curso.id FROM CursoClienteAcesso a WHERE a.cliente.id = :clienteId")
	List<Long> buscarCursoIdsPorClienteId(Long clienteId);

}
