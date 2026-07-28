package br.com.techgold.learn.dto;

import java.util.List;

public record DtoCursoProgresso(
		Long cursoId,
		String titulo,
		List<DtoAulaProgresso> aulas
		) {}
