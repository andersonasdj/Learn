package br.com.techgold.learn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoAtualizarConteudo(
		@NotNull
		Long id,
		String titulo,
		@NotBlank
		String corpoHtml,
		int ordem
		) {}
