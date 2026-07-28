package br.com.techgold.learn.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.techgold.learn.dto.DtoAtualizarConteudo;
import br.com.techgold.learn.dto.DtoCadastroConteudo;
import br.com.techgold.learn.dto.DtoConteudoList;
import br.com.techgold.learn.dto.DtoImagemUpload;
import br.com.techgold.learn.services.ConteudoService;
import br.com.techgold.learn.services.ImagemArmazenamentoService;

@RestController
@RequestMapping("conteudos")
@PreAuthorize("hasRole('ROLE_EDITOR')")
public class ConteudoRestController {

	@Autowired private ConteudoService service;
	@Autowired private ImagemArmazenamentoService armazenamentoService;

	@GetMapping("/aula/{aulaId}")
	public ResponseEntity<List<DtoConteudoList>> listarPorAula(@PathVariable Long aulaId) {
		return ResponseEntity.ok(service.listarPorAula(aulaId));
	}

	@GetMapping("/edit/{id}")
	public ResponseEntity<DtoConteudoList> editar(@PathVariable Long id) {
		return ResponseEntity.ok(service.buscarParaEdicao(id));
	}

	@PostMapping
	public void cadastrar(@RequestBody DtoCadastroConteudo dados) {
		service.cadastrarNovoConteudo(dados);
	}

	@PutMapping
	public void atualizar(@RequestBody DtoAtualizarConteudo dados) {
		service.atualizarConteudo(dados);
	}

	@PostMapping(value = "/imagens", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> enviarImagem(@RequestParam Long aulaId, @RequestParam("file") MultipartFile file) {
		try {
			String caminho = armazenamentoService.salvar(file, aulaId);
			return ResponseEntity.ok(new DtoImagemUpload(caminho));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{id}")
	public void deletar(@PathVariable Long id) {
		service.excluirConteudo(id);
	}

}
