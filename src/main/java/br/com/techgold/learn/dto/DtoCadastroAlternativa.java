package br.com.techgold.learn.dto;

import jakarta.validation.constraints.NotBlank;

public record DtoCadastroAlternativa(
		@NotBlank
		String texto,
		boolean correta
		) {}
