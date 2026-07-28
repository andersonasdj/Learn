package br.com.techgold.learn.dto;

import java.util.Comparator;
import java.util.List;

import br.com.techgold.learn.model.Questao;
import br.com.techgold.learn.model.Teste;

public record DtoTeste(
		Long id,
		Long aulaId,
		String titulo,
		int notaAprovacao,
		int tentativasMaximas,
		List<DtoQuestao> questoes
		) {

	public DtoTeste(Teste t) {
		this(t.getId(), t.getAula().getId(), t.getTitulo(), t.getNotaAprovacao(), t.getTentativasMaximas(),
				t.getQuestoes().stream()
						.sorted(Comparator.comparingInt(Questao::getOrdem))
						.map(DtoQuestao::new)
						.toList());
	}
}
