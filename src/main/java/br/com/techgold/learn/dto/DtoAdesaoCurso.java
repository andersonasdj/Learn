package br.com.techgold.learn.dto;

import java.util.List;

public record DtoAdesaoCurso(
		Long cursoId,
		String tituloCurso,
		int totalAlunos,
		List<DtoAulaAdesao> aulas
		) {}
