package br.com.techgold.learn.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgold.learn.dto.DtoAtualizarTeste;
import br.com.techgold.learn.dto.DtoCadastroAlternativa;
import br.com.techgold.learn.dto.DtoCadastroQuestao;
import br.com.techgold.learn.dto.DtoCadastroTeste;
import br.com.techgold.learn.dto.DtoTeste;
import br.com.techgold.learn.model.Aula;
import br.com.techgold.learn.model.Teste;
import br.com.techgold.learn.repository.AulaRepository;
import br.com.techgold.learn.repository.TesteRepository;
import jakarta.transaction.Transactional;

@Service
public class TesteService {

	@Autowired private TesteRepository repository;
	@Autowired private AulaRepository repositoryAula;

	public DtoTeste buscarPorAula(Long aulaId) {
		return repository.findByAulaId(aulaId).map(DtoTeste::new).orElse(null);
	}

	@Transactional
	public void cadastrarNovoTeste(DtoCadastroTeste dados) {
		if (repository.existsByAulaId(dados.aulaId())) {
			throw new IllegalStateException("Esta aula já possui um teste");
		}
		validarQuestoes(dados.questoes());
		Aula aula = repositoryAula.getReferenceById(dados.aulaId());
		repository.save(new Teste(dados, aula));
	}

	@Transactional
	public DtoTeste atualizarTeste(DtoAtualizarTeste dados) {
		validarQuestoes(dados.questoes());
		Teste teste = repository.findById(dados.id())
				.orElseThrow(() -> new IllegalArgumentException("Teste não encontrado"));
		teste.setTitulo(dados.titulo());
		teste.setNotaAprovacao(dados.notaAprovacao());
		teste.setTentativasMaximas(dados.tentativasMaximas());
		teste.repopularQuestoes(dados.questoes());
		return new DtoTeste(repository.save(teste));
	}

	private void validarQuestoes(List<DtoCadastroQuestao> questoes) {
		for (DtoCadastroQuestao questao : questoes) {
			List<DtoCadastroAlternativa> alternativas = questao.alternativas();
			if (alternativas.size() < 2) {
				throw new IllegalArgumentException("Cada questão deve ter ao menos duas alternativas");
			}
			long corretas = alternativas.stream().filter(DtoCadastroAlternativa::correta).count();
			if (corretas != 1) {
				throw new IllegalArgumentException("Cada questão deve ter exatamente uma alternativa correta");
			}
		}
	}

}
