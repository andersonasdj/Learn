package br.com.techgold.learn.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgold.learn.dto.DtoAtualizarCurso;
import br.com.techgold.learn.dto.DtoCadastroCurso;
import br.com.techgold.learn.dto.DtoCursoList;
import br.com.techgold.learn.repository.CursoRepository;
import br.com.techgold.learn.services.CursoService;

@RestController
@RequestMapping("cursos")
@PreAuthorize("hasRole('ROLE_EDITOR')")
public class CursoRestController {

	@Autowired private CursoRepository repository;
	@Autowired private CursoService service;

	@GetMapping
	public ResponseEntity<List<DtoCursoList>> listar() {
		return ResponseEntity.ok(service.listarTodos());
	}

	@GetMapping("/edit/{id}")
	public ResponseEntity<DtoAtualizarCurso> editar(@PathVariable Long id) {
		return ResponseEntity.ok(service.buscarParaEdicao(id));
	}

	@PostMapping
	public ResponseEntity<Long> cadastrar(@RequestBody DtoCadastroCurso dados) {
		return ResponseEntity.ok(service.cadastrarNovoCurso(dados));
	}

	@PutMapping
	public ResponseEntity<DtoAtualizarCurso> atualizar(@RequestBody DtoAtualizarCurso dados) {
		return ResponseEntity.ok(new DtoAtualizarCurso(service.atualizarCurso(dados)));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{id}")
	public void deletar(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
