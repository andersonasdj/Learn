package br.com.techgold.learn.dto;

import java.util.List;

public record DtoTesteProgresso(
		Long id,
		String titulo,
		int notaAprovacao,
		int tentativasMaximas,
		boolean liberado,
		boolean aprovado,
		int tentativas,
		Integer notaObtida,
		boolean bloqueado,
		List<DtoQuestaoAluno> questoes
		) {}
