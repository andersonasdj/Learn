package br.com.techgold.learn.dto;

import java.util.List;

public record DtoAulaProgresso(
		Long id,
		String titulo,
		int ordem,
		boolean liberada,
		boolean concluida,
		List<DtoVideoProgresso> videos,
		DtoTesteProgresso teste
		) {}
