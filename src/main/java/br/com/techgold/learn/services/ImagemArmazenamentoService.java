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
public class ImagemArmazenamentoService {

	private static final Set<String> EXTENSOES_PERMITIDAS = Set.of("png", "jpg", "jpeg", "gif", "webp");

	@Value("${upload.dir}")
	private String uploadDir;

	public String salvar(MultipartFile arquivo, Long aulaId) {
		if (arquivo == null || arquivo.isEmpty()) {
			throw new IllegalArgumentException("Selecione uma imagem.");
		}

		String extensao = extrairExtensao(arquivo.getOriginalFilename());
		if (!EXTENSOES_PERMITIDAS.contains(extensao)) {
			throw new IllegalArgumentException("Formato de imagem não suportado: " + extensao);
		}

		String caminhoRelativo = "imagens/" + aulaId + "/" + UUID.randomUUID() + "." + extensao;
		try {
			Path destino = Path.of(uploadDir, caminhoRelativo);
			Files.createDirectories(destino.getParent());
			arquivo.transferTo(destino);
		} catch (IOException e) {
			throw new UncheckedIOException("Erro ao salvar a imagem", e);
		}
		return caminhoRelativo;
	}

	private String extrairExtensao(String nomeOriginal) {
		if (nomeOriginal == null || !nomeOriginal.contains(".")) return "";
		return nomeOriginal.substring(nomeOriginal.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
	}

}
