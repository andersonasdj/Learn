package br.com.techgold.learn.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record DtoAtualizarModeloCertificado(
		@NotBlank
		String caminhoImagemFundo,
		int larguraImagemPx,
		int alturaImagemPx,
		@NotEmpty
		List<DtoCampoCertificado> campos
		) {}
