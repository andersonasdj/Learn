package br.com.techgold.learn.dto;

import java.io.Serializable;

import br.com.techgold.learn.model.Aula;

public record DtoAulaList(
		Long id,
		Long cursoId,
		String titulo,
		int ordem
		) implements Serializable {

	public DtoAulaList(Aula a) {
		this(a.getId(), a.getCurso().getId(), a.getTitulo(), a.getOrdem());
	}
}
