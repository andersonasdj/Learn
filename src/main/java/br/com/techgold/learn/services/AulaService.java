package br.com.techgold.learn.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgold.learn.dto.DtoAtualizarAula;
import br.com.techgold.learn.dto.DtoAulaList;
import br.com.techgold.learn.dto.DtoCadastroAula;
import br.com.techgold.learn.model.Aula;
import br.com.techgold.learn.model.Curso;
import br.com.techgold.learn.repository.AulaRepository;
import br.com.techgold.learn.repository.CursoRepository;
import jakarta.transaction.Transactional;

@Service
public class AulaService {

	@Autowired private AulaRepository repository;
	@Autowired private CursoRepository repositoryCurso;

	public List<DtoAulaList> listarPorCurso(Long cursoId) {
		return repository.findByCursoIdOrderByOrdemAsc(cursoId).stream().map(DtoAulaList::new).toList();
	}

	public DtoAtualizarAula buscarParaEdicao(Long id) {
		if (repository.existsById(id)) {
			return new DtoAtualizarAula(repository.getReferenceById(id));
		} else {
			return null;
		}
	}

	public void cadastrarNovaAula(DtoCadastroAula dados) {
		Curso curso = repositoryCurso.getReferenceById(dados.cursoId());
		repository.save(new Aula(dados, curso));
	}

	public Aula atualizarAula(DtoAtualizarAula dados) {
		Curso curso = repository.getReferenceById(dados.id()).getCurso();
		return repository.save(new Aula(dados, curso));
	}

	@Transactional
	public void reordenar(List<Long> aulaIdsEmOrdem) {
		for (int i = 0; i < aulaIdsEmOrdem.size(); i++) {
			Aula aula = repository.getReferenceById(aulaIdsEmOrdem.get(i));
			aula.setOrdem(i);
		}
	}

}
