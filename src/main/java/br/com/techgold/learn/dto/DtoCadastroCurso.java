package br.com.techgold.learn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoCadastroCurso(
		@NotBlank
		String titulo,
		String descricao,
		String caminhoCapa,
		@NotNull
		Long autorId
		) {}
