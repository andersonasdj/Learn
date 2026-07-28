package br.com.techgold.learn.dto;

import java.util.Comparator;
import java.util.List;

import br.com.techgold.learn.model.Alternativa;
import br.com.techgold.learn.model.Questao;

public record DtoQuestaoAluno(
		Long id,
		String enunciado,
		List<DtoAlternativaAluno> alternativas
		) {

	public DtoQuestaoAluno(Questao q) {
		this(q.getId(), q.getEnunciado(),
				q.getAlternativas().stream()
						.sorted(Comparator.comparingInt(Alternativa::getOrdem))
						.map(DtoAlternativaAluno::new)
						.toList());
	}
}
