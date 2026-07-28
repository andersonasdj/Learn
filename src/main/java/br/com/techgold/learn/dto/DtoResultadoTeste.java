package br.com.techgold.learn.dto;

import java.util.List;

public record DtoResultadoTeste(
		int acertos,
		int total,
		int percentual,
		boolean aprovado,
		int tentativas,
		int tentativasRestantes,
		boolean bloqueado,
		List<DtoCorrecaoQuestao> correcoes
		) {}
