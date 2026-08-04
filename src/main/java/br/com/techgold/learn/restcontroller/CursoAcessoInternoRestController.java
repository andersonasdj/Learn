package br.com.techgold.learn.restcontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgold.learn.dto.DtoCursoList;
import br.com.techgold.learn.services.CursoAcessoService;

/**
 * Endpoints de consulta para o futuro microserviço de consumo de cursos
 * (Cliente/Colaborador). Ainda sem autenticação própria — hoje só é
 * alcançável por uma sessão autenticada nesta aplicação; um mecanismo de
 * autenticação máquina-a-máquina precisa ser definido antes de expor este
 * prefixo a um chamador externo.
 */
@RestController
@RequestMapping("api/interno")
public class CursoAcessoInternoRestController {

	private final CursoAcessoService service;

	CursoAcessoInternoRestController(CursoAcessoService service) {
		this.service = service;
	}

	@GetMapping("/cursos/acesso")
	public ResponseEntity<Boolean> temAcesso(@RequestParam Long colaboradorId, @RequestParam Long cursoId) {
		return ResponseEntity.ok(service.temAcesso(colaboradorId, cursoId));
	}

	@GetMapping("/colaboradores/{colaboradorId}/cursos")
	public ResponseEntity<List<DtoCursoList>> listarCursosAcessiveis(@PathVariable Long colaboradorId) {
		return ResponseEntity.ok(service.listarCursosAcessiveis(colaboradorId));
	}

}
