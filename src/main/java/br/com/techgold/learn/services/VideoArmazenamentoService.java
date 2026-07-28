package br.com.techgold.learn.services;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoArmazenamentoService {

	private static final Set<String> EXTENSOES_PERMITIDAS = Set.of("mp4", "webm", "ogg", "ogv", "mov", "mkv", "avi");

	@Value("${upload.dir}")
	private String uploadDir;

	public String salvar(MultipartFile arquivo, Long aulaId) {
		if (arquivo == null || arquivo.isEmpty()) {
			throw new IllegalArgumentException("Selecione um arquivo de vídeo.");
		}

		String extensao = extrairExtensao(arquivo.getOriginalFilename());
		if (!EXTENSOES_PERMITIDAS.contains(extensao)) {
			throw new IllegalArgumentException("Formato de vídeo não suportado: " + extensao);
		}

		String caminhoRelativo = "videos/" + aulaId + "/" + UUID.randomUUID() + "." + extensao;
		try {
			Path destino = Path.of(uploadDir, caminhoRelativo);
			Files.createDirectories(destino.getParent());
			arquivo.transferTo(destino);
		} catch (IOException e) {
			throw new UncheckedIOException("Erro ao salvar o arquivo de vídeo", e);
		}
		return caminhoRelativo;
	}

	public void excluir(String caminhoRelativo) {
		if (caminhoRelativo == null || caminhoRelativo.isBlank()) return;
		try {
			Files.deleteIfExists(Path.of(uploadDir, caminhoRelativo));
		} catch (IOException e) {
			// arquivo pode já ter sido removido externamente; segue sem interromper a exclusão do registro
		}
	}

	private String extrairExtensao(String nomeOriginal) {
		if (nomeOriginal == null || !nomeOriginal.contains(".")) return "";
		return nomeOriginal.substring(nomeOriginal.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
	}

}
