package br.com.techgold.learn.services;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.techgold.learn.dto.DtoCertificado;
import br.com.techgold.learn.dto.DtoCertificadoEmitido;
import br.com.techgold.learn.model.Aula;
import br.com.techgold.learn.model.CampoCertificado;
import br.com.techgold.learn.model.Certificado;
import br.com.techgold.learn.model.Curso;
import br.com.techgold.learn.model.Funcionario;
import br.com.techgold.learn.model.ModeloCertificado;
import br.com.techgold.learn.model.Video;
import br.com.techgold.learn.model.enums.AlinhamentoTexto;
import br.com.techgold.learn.repository.AulaRepository;
import br.com.techgold.learn.repository.CertificadoRepository;
import br.com.techgold.learn.repository.CursoRepository;
import br.com.techgold.learn.repository.FuncionarioRepository;
import br.com.techgold.learn.repository.ModeloCertificadoRepository;
import br.com.techgold.learn.repository.VideoRepository;
import jakarta.transaction.Transactional;

@Service
public class CertificadoService {

	@Autowired private CertificadoRepository repository;
	@Autowired private ModeloCertificadoRepository repositoryModelo;
	@Autowired private CursoRepository repositoryCurso;
	@Autowired private AulaRepository repositoryAula;
	@Autowired private VideoRepository repositoryVideo;
	@Autowired private FuncionarioRepository repositoryFuncionario;
	@Autowired private ProgressoService progressoService;

	@Value("${upload.dir}")
	private String uploadDir;

	public DtoCertificado buscarPorCurso(Long cursoId) {
		Long funcionarioId = progressoService.funcionarioLogadoId();
		return repository.findByFuncionarioIdAndCursoId(funcionarioId, cursoId).map(DtoCertificado::new).orElse(null);
	}

	public List<DtoCertificadoEmitido> listarTodos() {
		return repository.findAllByOrderByDataEmissaoDesc().stream().map(DtoCertificadoEmitido::new).toList();
	}

	@Transactional
	public DtoCertificado emitirOuReaproveitar(Long cursoId) {
		if (!progressoService.cursoConcluidoPeloFuncionarioLogado(cursoId)) {
			throw new IllegalStateException("Este curso ainda não foi concluído.");
		}

		Long funcionarioId = progressoService.funcionarioLogadoId();

		return repository.findByFuncionarioIdAndCursoId(funcionarioId, cursoId)
				.map(DtoCertificado::new)
				.orElseGet(() -> {
					Funcionario funcionario = repositoryFuncionario.getReferenceById(funcionarioId);
					Curso curso = repositoryCurso.getReferenceById(cursoId);
					double cargaHoraria = calcularCargaHorariaHoras(cursoId);
					Certificado novo = new Certificado(funcionario, curso, cargaHoraria);
					return new DtoCertificado(repository.save(novo));
				});
	}

	public byte[] gerarPdf(Long certificadoId) {
		Certificado certificado = repository.findById(certificadoId)
				.orElseThrow(() -> new IllegalArgumentException("Certificado não encontrado"));

		List<ModeloCertificado> modelos = repositoryModelo.findAll();
		if (modelos.isEmpty()) {
			throw new IllegalStateException("Nenhum modelo de certificado configurado");
		}
		ModeloCertificado modelo = modelos.get(0);

		try (PDDocument documento = new PDDocument()) {
			float largura = modelo.getLarguraImagemPx();
			float altura = modelo.getAlturaImagemPx();
			PDPage pagina = new PDPage(new PDRectangle(largura, altura));
			documento.addPage(pagina);

			PDImageXObject imagemFundo = PDImageXObject.createFromFile(
					Path.of(uploadDir, modelo.getCaminhoImagemFundo()).toString(), documento);

			try (PDPageContentStream conteudo = new PDPageContentStream(documento, pagina)) {
				conteudo.drawImage(imagemFundo, 0, 0, largura, altura);

				for (CampoCertificado campo : modelo.getCampos()) {
					desenharCampo(conteudo, campo, certificado, largura, altura);
				}
			}

			ByteArrayOutputStream saida = new ByteArrayOutputStream();
			documento.save(saida);
			return saida.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException("Erro ao gerar o certificado em PDF", e);
		}
	}

	private void desenharCampo(PDPageContentStream conteudo, CampoCertificado campo, Certificado certificado,
			float largura, float altura) throws IOException {
		String texto = resolverTexto(campo.getTipoCampo(), certificado);
		PDFont fonte = campo.isNegrito()
				? new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD)
				: new PDType1Font(Standard14Fonts.FontName.HELVETICA);
		float tamanhoFonte = campo.getTamanhoFonte();
		float larguraTexto = fonte.getStringWidth(texto) / 1000f * tamanhoFonte;

		float x = (float) (campo.getPosXPercentual() / 100.0 * largura);
		float y = altura - (float) (campo.getPosYPercentual() / 100.0 * altura);

		if (campo.getAlinhamento() == AlinhamentoTexto.CENTRO) {
			x -= larguraTexto / 2;
		} else if (campo.getAlinhamento() == AlinhamentoTexto.DIREITA) {
			x -= larguraTexto;
		}

		Color cor = campo.getCorHex() != null && !campo.getCorHex().isBlank()
				? Color.decode(campo.getCorHex())
				: Color.BLACK;

		conteudo.beginText();
		conteudo.setFont(fonte, tamanhoFonte);
		conteudo.setNonStrokingColor(cor);
		conteudo.newLineAtOffset(x, y);
		conteudo.showText(texto);
		conteudo.endText();
	}

	private String resolverTexto(br.com.techgold.learn.model.enums.CampoCertificadoTipo tipo, Certificado certificado) {
		return switch (tipo) {
			case NOME_ALUNO -> certificado.getFuncionario().getNomeFuncionario();
			case NOME_CURSO -> certificado.getCurso().getTitulo();
			case DATA_CONCLUSAO -> certificado.getDataEmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			case CARGA_HORARIA -> formatarCargaHoraria(certificado.getCargaHorariaHorasNaEmissao());
			case CODIGO_VALIDACAO -> certificado.getCodigoValidacao();
		};
	}

	private String formatarCargaHoraria(double horas) {
		if (horas == Math.floor(horas)) {
			return (int) horas + "h";
		}
		return String.format(Locale.forLanguageTag("pt-BR"), "%.1fh", horas);
	}

	private double calcularCargaHorariaHoras(Long cursoId) {
		List<Aula> aulas = repositoryAula.findByCursoIdOrderByOrdemAsc(cursoId);
		int totalSegundos = 0;
		for (Aula aula : aulas) {
			for (Video video : repositoryVideo.findByAulaIdOrderByOrdemAsc(aula.getId())) {
				if (video.getDuracaoSegundos() != null) {
					totalSegundos += video.getDuracaoSegundos();
				}
			}
		}
		return totalSegundos / 3600.0;
	}

}
