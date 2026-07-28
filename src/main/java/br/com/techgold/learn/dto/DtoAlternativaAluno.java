package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.Alternativa;

public record DtoAlternativaAluno(
		Long id,
		String texto
		) {

	public DtoAlternativaAluno(Alternativa a) {
		this(a.getId(), a.getTexto());
	}
}
