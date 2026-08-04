package br.com.techgold.learn.restcontroller;

import java.util.List;

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

import br.com.techgold.learn.dto.DtoAtualizarAula;
import br.com.techgold.learn.dto.DtoAulaList;
import br.com.techgold.learn.dto.DtoCadastroAula;
import br.com.techgold.learn.repository.AulaRepository;
import br.com.techgold.learn.services.AulaService;

@RestController
@RequestMapping("aulas")
@PreAuthorize("hasRole('ROLE_EDITOR')")
public class AulaRestController {

	private final AulaRepository repository;
	private final AulaService service;

	AulaRestController(AulaRepository repository, AulaService service) {
		this.repository = repository;
		this.service = service;
	}

	@GetMapping("/curso/{cursoId}")
	public ResponseEntity<List<DtoAulaList>> listarPorCurso(@PathVariable Long cursoId) {
		return ResponseEntity.ok(service.listarPorCurso(cursoId));
	}

	@GetMapping("/edit/{id}")
	public ResponseEntity<DtoAtualizarAula> editar(@PathVariable Long id) {
		return ResponseEntity.ok(service.buscarParaEdicao(id));
	}

	@PostMapping
	public void cadastrar(@RequestBody DtoCadastroAula dados) {
		service.cadastrarNovaAula(dados);
	}

	@PutMapping
	public ResponseEntity<DtoAtualizarAula> atualizar(@RequestBody DtoAtualizarAula dados) {
		return ResponseEntity.ok(new DtoAtualizarAula(service.atualizarAula(dados)));
	}

	@PutMapping("/reordenar")
	public void reordenar(@RequestBody List<Long> aulaIdsEmOrdem) {
		service.reordenar(aulaIdsEmOrdem);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{id}")
	public void deletar(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
