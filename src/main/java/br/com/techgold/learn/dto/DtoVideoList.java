package br.com.techgold.learn.dto;

import java.io.Serializable;

import br.com.techgold.learn.model.Video;

public record DtoVideoList(
		Long id,
		Long aulaId,
		String titulo,
		String url,
		String nomeArquivoOriginal,
		Integer duracaoSegundos,
		int ordem
		) implements Serializable {

	public DtoVideoList(Video v) {
		this(v.getId(), v.getAula().getId(), v.getTitulo(), v.getUrl(), v.getNomeArquivoOriginal(),
				v.getDuracaoSegundos(), v.getOrdem());
	}
}
