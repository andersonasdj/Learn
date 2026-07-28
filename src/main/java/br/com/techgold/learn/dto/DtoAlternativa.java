package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.Alternativa;

public record DtoAlternativa(
		Long id,
		String texto,
		boolean correta,
		int ordem
		) {

	public DtoAlternativa(Alternativa a) {
		this(a.getId(), a.getTexto(), a.isCorreta(), a.getOrdem());
	}
}
