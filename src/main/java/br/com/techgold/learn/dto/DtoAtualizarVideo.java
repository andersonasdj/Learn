package br.com.techgold.learn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoAtualizarVideo(
		@NotNull
		Long id,
		@NotBlank
		String titulo,
		Integer duracaoSegundos,
		int ordem
		) {}
