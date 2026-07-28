package br.com.techgold.learn.dto;

import jakarta.validation.constraints.NotNull;

public record DtoCadastroCursoColaboradorAcesso(
		@NotNull
		Long cursoId,
		@NotNull
		Long colaboradorId,
		@NotNull
		Long concedidoPorId
		) {}
