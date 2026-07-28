package br.com.techgold.learn.restcontroller;

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

import br.com.techgold.learn.dto.DtoAtualizarProgressoVideo;
import br.com.techgold.learn.dto.DtoCursoProgresso;
import br.com.techgold.learn.dto.DtoResponderTeste;
import br.com.techgold.learn.dto.DtoResultadoTeste;
import br.com.techgold.learn.services.ProgressoService;

@RestController
@RequestMapping("progresso")
@PreAuthorize("hasRole('ROLE_USER')")
public class ProgressoRestController {

	@Autowired private ProgressoService service;

	@GetMapping("/cursos/{cursoId}")
	public ResponseEntity<DtoCursoProgresso> obterProgresso(@PathVariable Long cursoId) {
		return ResponseEntity.ok(service.obterProgresso(cursoId));
	}

	@PutMapping("/videos/{videoId}")
	public ResponseEntity<?> salvarProgressoVideo(@PathVariable Long videoId,
			@RequestBody DtoAtualizarProgressoVideo dados) {
		try {
			service.salvarProgressoVideo(videoId, dados);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/testes/{testeId}/responder")
	public ResponseEntity<?> responderTeste(@PathVariable Long testeId, @RequestBody DtoResponderTeste dados) {
		try {
			DtoResultadoTeste resultado = service.responderTeste(testeId, dados);
			return ResponseEntity.ok(resultado);
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/cursos/{cursoId}")
	public void reiniciar(@PathVariable Long cursoId) {
		service.reiniciarProgresso(cursoId);
	}

}
