package br.com.techgold.learn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoCadastroAula(
		@NotNull
		Long cursoId,
		@NotBlank
		String titulo,
		String descricao,
		int ordem
		) {}
