package br.com.techgold.learn.restcontroller;

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

import br.com.techgold.learn.dto.DtoAtualizarTeste;
import br.com.techgold.learn.dto.DtoCadastroTeste;
import br.com.techgold.learn.dto.DtoTeste;
import br.com.techgold.learn.repository.TesteRepository;
import br.com.techgold.learn.services.TesteService;

@RestController
@RequestMapping("testes")
@PreAuthorize("hasRole('ROLE_EDITOR')")
public class TesteRestController {

	private final TesteRepository repository;
	private final TesteService service;

	TesteRestController(TesteRepository repository, TesteService service) {
		this.repository = repository;
		this.service = service;
	}

	@GetMapping("/aula/{aulaId}")
	public ResponseEntity<DtoTeste> buscarPorAula(@PathVariable Long aulaId) {
		DtoTeste teste = service.buscarPorAula(aulaId);
		return teste != null ? ResponseEntity.ok(teste) : ResponseEntity.noContent().build();
	}

	@PostMapping
	public ResponseEntity<?> cadastrar(@RequestBody DtoCadastroTeste dados) {
		try {
			service.cadastrarNovoTeste(dados);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping
	public ResponseEntity<?> atualizar(@RequestBody DtoAtualizarTeste dados) {
		try {
			return ResponseEntity.ok(service.atualizarTeste(dados));
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{id}")
	public void deletar(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
