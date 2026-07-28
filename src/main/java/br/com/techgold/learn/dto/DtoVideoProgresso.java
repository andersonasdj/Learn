package br.com.techgold.learn.dto;

public record DtoVideoProgresso(
		Long id,
		String titulo,
		String url,
		Integer duracaoSegundos,
		int ordem,
		boolean liberado,
		boolean concluido,
		int posicaoSegundos
		) {}
