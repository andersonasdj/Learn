package br.com.techgold.learn.dto;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DtoCadastroTeste(
		@NotNull
		Long aulaId,
		String titulo,
		@Min(0) @Max(100)
		int notaAprovacao,
		@Min(1)
		int tentativasMaximas,
		@NotEmpty
		List<DtoCadastroQuestao> questoes
		) {}
