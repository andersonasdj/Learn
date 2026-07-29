package br.com.techgold.learn.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.techgold.learn.dto.DtoAtualizarCurso;
import br.com.techgold.learn.dto.DtoCadastroCurso;
import br.com.techgold.learn.dto.DtoCursoList;
import br.com.techgold.learn.model.Curso;
import br.com.techgold.learn.model.Funcionario;
import br.com.techgold.learn.repository.CursoRepository;
import br.com.techgold.learn.repository.FuncionarioRepository;

@Service
public class CursoService {

	@Autowired private CursoRepository repository;
	@Autowired private FuncionarioRepository repositoryFuncionario;
	@Autowired private ImagemArmazenamentoService armazenamentoImagem;

	public List<DtoCursoList> listarTodos() {
		return repository.findAll().stream().map(DtoCursoList::new).toList();
	}

	public DtoAtualizarCurso buscarParaEdicao(Long id) {
		if (repository.existsById(id)) {
			return new DtoAtualizarCurso(repository.getReferenceById(id));
		} else {
			return null;
		}
	}

	public Long cadastrarNovoCurso(DtoCadastroCurso dados) {
		Funcionario autor = repositoryFuncionario.getReferenceById(dados.autorId());
		return repository.save(new Curso(dados, autor)).getId();
	}

	public Curso atualizarCurso(DtoAtualizarCurso dados) {
		Curso existente = repository.getReferenceById(dados.id());
		return repository.save(new Curso(dados, existente.getAutor()));
	}

	public DtoAtualizarCurso atualizarCapa(Long id, MultipartFile arquivo) {
		Curso curso = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Curso não encontrado"));
		String caminho = armazenamentoImagem.salvarCapaCurso(arquivo, id);
		curso.setCaminhoCapa(caminho);
		return new DtoAtualizarCurso(repository.save(curso));
	}

}
