package br.com.techgold.learn.dto;

public record DtoCorrecaoQuestao(
		Long questaoId,
		Long alternativaCorretaId,
		Long alternativaEscolhidaId,
		boolean acertou
		) {}
