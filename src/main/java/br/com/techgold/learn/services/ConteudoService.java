package br.com.techgold.learn.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgold.learn.dto.DtoAtualizarConteudo;
import br.com.techgold.learn.dto.DtoCadastroConteudo;
import br.com.techgold.learn.dto.DtoConteudoList;
import br.com.techgold.learn.model.Aula;
import br.com.techgold.learn.model.Conteudo;
import br.com.techgold.learn.repository.AulaRepository;
import br.com.techgold.learn.repository.ConteudoRepository;

@Service
public class ConteudoService {

	@Autowired private ConteudoRepository repository;
	@Autowired private AulaRepository repositoryAula;

	public List<DtoConteudoList> listarPorAula(Long aulaId) {
		return repository.findByAulaIdOrderByOrdemAsc(aulaId).stream().map(DtoConteudoList::new).toList();
	}

	public DtoConteudoList buscarParaEdicao(Long id) {
		return repository.findById(id).map(DtoConteudoList::new).orElse(null);
	}

	public void cadastrarNovoConteudo(DtoCadastroConteudo dados) {
		Aula aula = repositoryAula.getReferenceById(dados.aulaId());
		repository.save(new Conteudo(dados, aula));
	}

	public void atualizarConteudo(DtoAtualizarConteudo dados) {
		Conteudo conteudo = repository.findById(dados.id())
				.orElseThrow(() -> new IllegalArgumentException("Conteúdo não encontrado"));
		conteudo.setTitulo(dados.titulo());
		conteudo.setCorpoHtml(dados.corpoHtml());
		conteudo.setOrdem(dados.ordem());
		repository.save(conteudo);
	}

	public void excluirConteudo(Long id) {
		repository.deleteById(id);
	}

}
