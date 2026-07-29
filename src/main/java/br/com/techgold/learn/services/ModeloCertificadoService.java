package br.com.techgold.learn.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.techgold.learn.dto.DtoAtualizarModeloCertificado;
import br.com.techgold.learn.dto.DtoImagemModeloUpload;
import br.com.techgold.learn.dto.DtoModeloCertificado;
import br.com.techgold.learn.model.ModeloCertificado;
import br.com.techgold.learn.repository.ModeloCertificadoRepository;

@Service
public class ModeloCertificadoService {

	@Autowired private ModeloCertificadoRepository repository;
	@Autowired private ImagemArmazenamentoService armazenamentoImagem;

	@Value("${upload.dir}")
	private String uploadDir;

	public DtoModeloCertificado obterModelo() {
		List<ModeloCertificado> modelos = repository.findAll();
		return modelos.isEmpty() ? null : new DtoModeloCertificado(modelos.get(0));
	}

	public DtoImagemModeloUpload enviarImagem(MultipartFile arquivo) {
		String caminho = armazenamentoImagem.salvarImagemCertificado(arquivo);
		try {
			BufferedImage imagem = ImageIO.read(Path.of(uploadDir, caminho).toFile());
			if (imagem == null) {
				throw new IllegalArgumentException("Não foi possível ler as dimensões da imagem.");
			}
			return new DtoImagemModeloUpload(caminho, imagem.getWidth(), imagem.getHeight());
		} catch (IOException e) {
			throw new UncheckedIOException("Erro ao ler a imagem enviada", e);
		}
	}

	public DtoModeloCertificado atualizarModelo(DtoAtualizarModeloCertificado dados) {
		List<ModeloCertificado> existentes = repository.findAll();
		ModeloCertificado modelo = existentes.isEmpty() ? new ModeloCertificado() : existentes.get(0);
		modelo.aplicar(dados);
		return new DtoModeloCertificado(repository.save(modelo));
	}

}
