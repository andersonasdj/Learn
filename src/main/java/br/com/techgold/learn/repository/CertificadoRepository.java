package br.com.techgold.learn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgold.learn.model.Certificado;

public interface CertificadoRepository extends JpaRepository<Certificado, Long> {

	Optional<Certificado> findByFuncionarioIdAndCursoId(Long funcionarioId, Long cursoId);

}
