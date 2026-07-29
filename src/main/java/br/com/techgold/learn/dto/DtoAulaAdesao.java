package br.com.techgold.learn.dto;

public record DtoAulaAdesao(
		Long aulaId,
		String titulo,
		int ordem,
		int concluidos,
		int emAndamento,
		int naoIniciados
		) {}
