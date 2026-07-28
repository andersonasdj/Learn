package br.com.techgold.learn.dto;

import jakarta.validation.constraints.NotNull;

public record DtoCadastroCursoClienteAcesso(
		@NotNull
		Long cursoId,
		@NotNull
		Long clienteId,
		@NotNull
		Long concedidoPorId
		) {}
