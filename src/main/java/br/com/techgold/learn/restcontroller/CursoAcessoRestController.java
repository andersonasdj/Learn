package br.com.techgold.learn.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgold.learn.dto.DtoAcessoClienteList;
import br.com.techgold.learn.dto.DtoAcessoColaboradorList;
import br.com.techgold.learn.dto.DtoCadastroCursoClienteAcesso;
import br.com.techgold.learn.dto.DtoCadastroCursoColaboradorAcesso;
import br.com.techgold.learn.services.CursoAcessoService;

@RestController
@RequestMapping("cursos/acesso")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CursoAcessoRestController {

	@Autowired private CursoAcessoService service;

	@PostMapping("/cliente")
	public ResponseEntity<?> liberarParaCliente(@RequestBody DtoCadastroCursoClienteAcesso dados) {
		try {
			service.liberarParaCliente(dados);
			return ResponseEntity.ok().build();
		} catch (IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/cliente/{id}")
	public void revogarDeCliente(@PathVariable Long id) {
		service.revogarDeCliente(id);
	}

	@GetMapping("/cliente/curso/{cursoId}")
	public ResponseEntity<List<DtoAcessoClienteList>> listarClientesComAcesso(@PathVariable Long cursoId) {
		return ResponseEntity.ok(service.listarClientesComAcesso(cursoId));
	}

	@PostMapping("/colaborador")
	public ResponseEntity<?> liberarParaColaborador(@RequestBody DtoCadastroCursoColaboradorAcesso dados) {
		try {
			service.liberarParaColaborador(dados);
			return ResponseEntity.ok().build();
		} catch (IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/colaborador/{id}")
	public void revogarDeColaborador(@PathVariable Long id) {
		service.revogarDeColaborador(id);
	}

	@GetMapping("/colaborador/curso/{cursoId}")
	public ResponseEntity<List<DtoAcessoColaboradorList>> listarColaboradoresComAcesso(@PathVariable Long cursoId) {
		return ResponseEntity.ok(service.listarColaboradoresComAcesso(cursoId));
	}

}
