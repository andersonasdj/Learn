package br.com.techgold.learn.dto;

import java.io.Serializable;

import br.com.techgold.learn.model.Conteudo;

public record DtoConteudoList(
		Long id,
		Long aulaId,
		String titulo,
		String corpoHtml,
		int ordem
		) implements Serializable {

	public DtoConteudoList(Conteudo c) {
		this(c.getId(), c.getAula().getId(), c.getTitulo(), c.getCorpoHtml(), c.getOrdem());
	}
}
