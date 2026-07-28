package br.com.techgold.learn.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record DtoCadastroQuestao(
		@NotBlank
		String enunciado,
		@NotEmpty
		List<DtoCadastroAlternativa> alternativas
		) {}
