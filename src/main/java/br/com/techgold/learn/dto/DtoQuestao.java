package br.com.techgold.learn.dto;

import java.util.Comparator;
import java.util.List;

import br.com.techgold.learn.model.Alternativa;
import br.com.techgold.learn.model.Questao;

public record DtoQuestao(
		Long id,
		String enunciado,
		int ordem,
		List<DtoAlternativa> alternativas
		) {

	public DtoQuestao(Questao q) {
		this(q.getId(), q.getEnunciado(), q.getOrdem(),
				q.getAlternativas().stream()
						.sorted(Comparator.comparingInt(Alternativa::getOrdem))
						.map(DtoAlternativa::new)
						.toList());
	}
}
