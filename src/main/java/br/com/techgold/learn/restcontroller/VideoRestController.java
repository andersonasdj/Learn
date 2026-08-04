package br.com.techgold.learn.restcontroller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.techgold.learn.dto.DtoAtualizarVideo;
import br.com.techgold.learn.dto.DtoCadastroVideo;
import br.com.techgold.learn.dto.DtoVideoList;
import br.com.techgold.learn.services.VideoService;

@RestController
@RequestMapping("videos")
@PreAuthorize("hasRole('ROLE_EDITOR')")
public class VideoRestController {

	private final VideoService service;

	VideoRestController(VideoService service) {
		this.service = service;
	}

	@GetMapping("/aula/{aulaId}")
	public ResponseEntity<List<DtoVideoList>> listarPorAula(@PathVariable Long aulaId) {
		return ResponseEntity.ok(service.listarPorAula(aulaId));
	}

	@GetMapping("/edit/{id}")
	public ResponseEntity<DtoVideoList> editar(@PathVariable Long id) {
		return ResponseEntity.ok(service.buscarParaEdicao(id));
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> cadastrar(
			@RequestParam Long aulaId,
			@RequestParam String titulo,
			@RequestParam(required = false) Integer duracaoSegundos,
			@RequestParam(defaultValue = "0") int ordem,
			@RequestParam("file") MultipartFile file) {
		try {
			DtoCadastroVideo dados = new DtoCadastroVideo(aulaId, titulo, duracaoSegundos, ordem);
			return ResponseEntity.ok(service.cadastrarNovoVideo(dados, file));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping
	public void atualizar(@RequestBody DtoAtualizarVideo dados) {
		service.atualizarVideo(dados);
	}

	@PutMapping(value = "/{id}/arquivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> substituirArquivo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
		try {
			return ResponseEntity.ok(service.substituirArquivo(id, file));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/reordenar")
	public void reordenar(@RequestBody List<Long> videoIdsEmOrdem) {
		service.reordenar(videoIdsEmOrdem);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{id}")
	public void deletar(@PathVariable Long id) {
		service.excluirVideo(id);
	}

}
