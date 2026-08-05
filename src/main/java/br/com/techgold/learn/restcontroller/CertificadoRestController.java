package br.com.techgold.learn.restcontroller;

import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgold.learn.dto.DtoCertificado;
import br.com.techgold.learn.dto.DtoCertificadoEmitido;
import br.com.techgold.learn.services.CertificadoService;

@RestController
@RequestMapping("certificados")
@PreAuthorize("hasRole('ROLE_USER')")
public class CertificadoRestController {

	private final CertificadoService service;

	CertificadoRestController(CertificadoService service) {
		this.service = service;
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	public ResponseEntity<List<DtoCertificadoEmitido>> listarTodos() {
		return ResponseEntity.ok(service.listarTodos());
	}

	@GetMapping("/curso/{cursoId}")
	public ResponseEntity<DtoCertificado> buscarPorCurso(@PathVariable Long cursoId) {
		DtoCertificado certificado = service.buscarPorCurso(cursoId);
		return certificado != null ? ResponseEntity.ok(certificado) : ResponseEntity.noContent().build();
	}

	@PostMapping("/curso/{cursoId}/emitir")
	public ResponseEntity<?> emitir(@PathVariable Long cursoId) {
		try {
			return ResponseEntity.ok(service.emitirOuReaproveitar(cursoId));
		} catch (IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{id}/pdf")
	public ResponseEntity<?> baixarPdf(@PathVariable Long id) {
		try {
			byte[] pdf = service.gerarPdf(id);
			ContentDisposition disposicao = ContentDisposition.inline()
					.filename("certificado-" + id + ".pdf")
					.build();
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_PDF)
					.header(HttpHeaders.CONTENT_DISPOSITION, disposicao.toString())
					.body(pdf);
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
