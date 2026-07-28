package br.com.techgold.learn.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.techgold.learn.dto.DtoAtualizarVideo;
import br.com.techgold.learn.dto.DtoCadastroVideo;
import br.com.techgold.learn.dto.DtoVideoList;
import br.com.techgold.learn.model.Aula;
import br.com.techgold.learn.model.Video;
import br.com.techgold.learn.repository.AulaRepository;
import br.com.techgold.learn.repository.VideoRepository;
import jakarta.transaction.Transactional;

@Service
public class VideoService {

	@Autowired private VideoRepository repository;
	@Autowired private AulaRepository repositoryAula;
	@Autowired private VideoArmazenamentoService armazenamentoService;

	public List<DtoVideoList> listarPorAula(Long aulaId) {
		return repository.findByAulaIdOrderByOrdemAsc(aulaId).stream().map(DtoVideoList::new).toList();
	}

	public DtoVideoList buscarParaEdicao(Long id) {
		return repository.findById(id).map(DtoVideoList::new).orElse(null);
	}

	public DtoVideoList cadastrarNovoVideo(DtoCadastroVideo dados, MultipartFile arquivo) {
		Aula aula = repositoryAula.getReferenceById(dados.aulaId());
		String caminhoArquivo = armazenamentoService.salvar(arquivo, dados.aulaId());
		Video video = new Video(dados, aula, caminhoArquivo, arquivo.getOriginalFilename());
		return new DtoVideoList(repository.save(video));
	}

	public void atualizarVideo(DtoAtualizarVideo dados) {
		Video video = repository.findById(dados.id())
				.orElseThrow(() -> new IllegalArgumentException("Vídeo não encontrado"));
		video.setTitulo(dados.titulo());
		video.setDuracaoSegundos(dados.duracaoSegundos());
		video.setOrdem(dados.ordem());
		repository.save(video);
	}

	public DtoVideoList substituirArquivo(Long id, MultipartFile arquivo) {
		Video video = repository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Vídeo não encontrado"));
		String caminhoAntigo = video.getUrl();
		String caminhoNovo = armazenamentoService.salvar(arquivo, video.getAula().getId());
		video.setUrl(caminhoNovo);
		video.setNomeArquivoOriginal(arquivo.getOriginalFilename());
		DtoVideoList atualizado = new DtoVideoList(repository.save(video));
		armazenamentoService.excluir(caminhoAntigo);
		return atualizado;
	}

	public void excluirVideo(Long id) {
		repository.findById(id).ifPresent(video -> {
			repository.deleteById(id);
			armazenamentoService.excluir(video.getUrl());
		});
	}

	@Transactional
	public void reordenar(List<Long> videoIdsEmOrdem) {
		for (int i = 0; i < videoIdsEmOrdem.size(); i++) {
			Video video = repository.getReferenceById(videoIdsEmOrdem.get(i));
			video.setOrdem(i);
		}
	}

}
