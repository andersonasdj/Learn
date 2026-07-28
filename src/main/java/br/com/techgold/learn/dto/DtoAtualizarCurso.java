package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.Curso;
import br.com.techgold.learn.model.enums.CursoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoAtualizarCurso(
		@NotNull
		Long id,
		@NotBlank
		String titulo,
		String descricao,
		String caminhoCapa,
		@NotNull
		CursoStatus status
		) {

	public DtoAtualizarCurso(Curso c) {
		this(c.getId(), c.getTitulo(), c.getDescricao(), c.getCaminhoCapa(), c.getStatus());
	}
}
