package br.com.techgold.learn.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.techgold.learn.dto.DtoAtualizarModeloCertificado;
import br.com.techgold.learn.dto.DtoModeloCertificado;
import br.com.techgold.learn.services.ModeloCertificadoService;

@RestController
@RequestMapping("modelo-certificado")
@PreAuthorize("hasRole('ROLE_EDITOR')")
public class ModeloCertificadoRestController {

	@Autowired private ModeloCertificadoService service;

	@GetMapping
	public ResponseEntity<DtoModeloCertificado> obter() {
		return ResponseEntity.ok(service.obterModelo());
	}

	@PostMapping(value = "/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> enviarImagem(@RequestParam("file") MultipartFile file) {
		try {
			return ResponseEntity.ok(service.enviarImagem(file));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping
	public ResponseEntity<DtoModeloCertificado> atualizar(@RequestBody DtoAtualizarModeloCertificado dados) {
		return ResponseEntity.ok(service.atualizarModelo(dados));
	}

}
