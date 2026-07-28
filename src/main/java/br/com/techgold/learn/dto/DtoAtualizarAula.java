package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.Aula;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoAtualizarAula(
		@NotNull
		Long id,
		@NotBlank
		String titulo,
		String descricao,
		int ordem
		) {

	public DtoAtualizarAula(Aula a) {
		this(a.getId(), a.getTitulo(), a.getDescricao(), a.getOrdem());
	}
}
