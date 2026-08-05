package br.com.techgold.learn.restcontroller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.techgold.learn.dto.DtoFile;
import br.com.techgold.learn.model.Funcionario;
import br.com.techgold.learn.services.FuncionarioService;

@RestController
@RequestMapping("/api/file")
public class FileRestController {
	
	final FuncionarioService service;

	@Value("${upload.dir}")
	private String UPLOAD_DIR;

	FileRestController(FuncionarioService service) {
		this.service = service;
	}
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@ModelAttribute DtoFile uploadRequest){
		MultipartFile file = uploadRequest.file();
		Long id = uploadRequest.id();
		
		if(file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("o arquivo está vazio");
		}
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			File uploadDir = new File(UPLOAD_DIR+"/solicitacoes/"+id+"/");
			if(!uploadDir.exists()) {
				boolean created = uploadDir.mkdirs();
				if(!created) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possivel criar o diretorio");
				}
			}
			
			File dest = new File(UPLOAD_DIR +"/solicitacoes/"+ id +"/" + fileName);
			file.transferTo(dest);
			
			return ResponseEntity.ok("Arquivo enviado com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo!");
		}
	}
	
	@PostMapping("/perfil/upload")
	public ResponseEntity<String> perfilUploadFile(@ModelAttribute DtoFile uploadRequest) {
	    Funcionario funcionario = service.buscaPorNome(
	        ((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario()
	    );

	    if (!uploadRequest.id().equals(funcionario.getId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não autorizado a alterar este perfil.");
	    }

	    MultipartFile file = uploadRequest.file();
	    Long id = uploadRequest.id();
	    String contentType = file.getContentType();
	    String originalFileName = file.getOriginalFilename();

	    if (!isValidImage(contentType, originalFileName)) {
	        return ResponseEntity.badRequest().body("Somente arquivos JPEG e PNG são permitidos");
	    }

	    if (file.isEmpty()) {
	        return ResponseEntity.badRequest().body("O arquivo está vazio");
	    }

	    try {
	        File uploadDir = new File(UPLOAD_DIR + "/perfil/" + id + "/");
	        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível criar o diretório");
	        }

	        // Limpa arquivos existentes
	        Files.list(uploadDir.toPath())
	            .filter(Files::isRegularFile)
	            .forEach(path -> {
	                try {
	                    Files.delete(path);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            });

	        BufferedImage originalImage = ImageIO.read(file.getInputStream());
	        if (originalImage == null) {
	            return ResponseEntity.badRequest().body("Arquivo inválido.");
	        }

	        // Cria nova imagem RGB com fundo branco (para remover transparência)
	        BufferedImage newImage = new BufferedImage(
	            originalImage.getWidth(),
	            originalImage.getHeight(),
	            BufferedImage.TYPE_INT_RGB
	        );
	        Graphics2D g = newImage.createGraphics();
	        g.setColor(Color.WHITE); // fundo branco
	        g.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
	        g.drawImage(originalImage, 0, 0, null);
	        g.dispose();

	        // Salva como JPEG com compressão
	        String finalFileName = "perfil_" + id + ".jpg";
	        Path destino = Paths.get(uploadDir.getAbsolutePath(), finalFileName);

	        try (OutputStream os = Files.newOutputStream(destino)) {
	            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
	            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
	            writer.setOutput(ios);

	            ImageWriteParam param = writer.getDefaultWriteParam();
	            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	            param.setCompressionQuality(0.3f); // 0.0 = baixa qualidade, 1.0 = alta

	            writer.write(null, new IIOImage(newImage, null, null), param);
	            ios.close();
	            writer.dispose();
	        }

	        service.atualizaImagem(funcionario.getId(), "/perfil/" + id + "/" + finalFileName);

	        try {
	            Path thumbPath = Paths.get(uploadDir.getAbsolutePath(), "perfil_" + id + "_thumb.jpg");
	            salvarThumb(newImage, thumbPath, 80);
	        } catch (IOException ignored) {}

	        return ResponseEntity.ok("Arquivo enviado com sucesso!");

	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo!");
	    }
	}
	
	private ResponseEntity<Resource> retornarImagemPadrao() {
		try {
			org.springframework.core.io.ClassPathResource defaultImg =
				new org.springframework.core.io.ClassPathResource("static/assets/img/login.png");
			if (defaultImg.exists()) {
				return ResponseEntity.ok()
					.contentType(MediaType.IMAGE_PNG)
					.body(defaultImg);
			}
		} catch (Exception ignored) {}
		return ResponseEntity.notFound().build();
	}

	private boolean isValidImage(String contentType, String fileName) {
		return (contentType != null && (
				contentType.equalsIgnoreCase("image/jpeg") ||
				contentType.equalsIgnoreCase("image/png")
				)) &&
				(fileName  != null && (
						fileName.toLowerCase().endsWith(".jpg") ||
						fileName.toLowerCase().endsWith(".jpeg") ||
						fileName.toLowerCase().endsWith(".png")
						));
	}
	
	@GetMapping("/perfil/{id}")
	public ResponseEntity<Resource> exibirImagem(@PathVariable Long id){
		
		Funcionario funcionario = service.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		
		if(id == funcionario.getId()) {
			Path diretorioImagens = Paths.get(UPLOAD_DIR);

			try {
				String caminhoFoto = funcionario.getCaminhoFoto();
				if(caminhoFoto == null || caminhoFoto.isBlank()) {
					return retornarImagemPadrao();
				}
				Path caminhoArquivo = diretorioImagens.resolve(diretorioImagens + caminhoFoto).normalize();
				Resource recurso = new UrlResource(caminhoArquivo.toUri());

				if(!recurso.exists()) {
					return retornarImagemPadrao();
				}
				
				String contentType = Files.probeContentType(caminhoArquivo);
				
				if(contentType == null) {
					contentType = "application/octet-stream";
				}
				
				return ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(contentType))
						.header("Content-Disposition", "inline; filename=\"" + recurso.getFilename() + "\"")
						.body(recurso);
				
			} catch (MalformedURLException e) {
				return ResponseEntity.badRequest().build();
			} catch (Exception e) {
				return ResponseEntity.internalServerError().build();
			}
		}else {
			return ResponseEntity.internalServerError().build();
		}
		
	}
	
	@GetMapping("/perfil/{id}/thumb")
	public ResponseEntity<Resource> exibirImagemThumb(@PathVariable Long id) {
		Funcionario funcionario = service.buscaPorNome(
			((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario()
		);

		if (!id.equals(funcionario.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		String caminhoFoto = funcionario.getCaminhoFoto();
		if (caminhoFoto == null || caminhoFoto.isBlank()) {
			return retornarImagemPadrao();
		}

		Path diretorioImagens = Paths.get(UPLOAD_DIR);
		Path caminhoArquivo = diretorioImagens.resolve(diretorioImagens + caminhoFoto).normalize();

		if (!caminhoArquivo.toFile().exists()) {
			return retornarImagemPadrao();
		}

		String nomeOriginal = caminhoArquivo.getFileName().toString();
		String nomeThumb = nomeOriginal.replaceFirst("(\\.jpg)$", "_thumb$1");
		Path caminhoThumb = caminhoArquivo.getParent().resolve(nomeThumb);

		if (!caminhoThumb.toFile().exists()) {
			try {
				BufferedImage original = ImageIO.read(caminhoArquivo.toFile());
				if (original != null) {
					salvarThumb(original, caminhoThumb, 80);
				}
			} catch (IOException ignored) {}
		}

		try {
			Path serveFrom = caminhoThumb.toFile().exists() ? caminhoThumb : caminhoArquivo;
			Resource recurso = new UrlResource(serveFrom.toUri());
			return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.header(HttpHeaders.CACHE_CONTROL, "max-age=86400, public")
				.header("Content-Disposition", "inline; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
		} catch (MalformedURLException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private void salvarThumb(BufferedImage original, Path destino, int size) throws IOException {
		int w = original.getWidth();
		int h = original.getHeight();
		int cropSize = Math.min(w, h);
		int x = (w - cropSize) / 2;
		int y = (h - cropSize) / 2;

		BufferedImage thumb = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = thumb.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, size, size);
		g.drawImage(original, 0, 0, size, size, x, y, x + cropSize, y + cropSize, null);
		g.dispose();

		try (OutputStream os = Files.newOutputStream(destino)) {
			ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
			ImageOutputStream ios = ImageIO.createImageOutputStream(os);
			writer.setOutput(ios);
			ImageWriteParam param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.82f);
			writer.write(null, new IIOImage(thumb, null, null), param);
			ios.close();
			writer.dispose();
		}
	}

	
	// ── Logo do cliente ───────────────────────────────────────────────────────
	@PostMapping("/cliente/{id}/logo")
	public ResponseEntity<String> uploadLogoCliente(
			@PathVariable Long id,
			@org.springframework.web.bind.annotation.RequestParam("file") MultipartFile file) {
		if (file == null || file.isEmpty()) return ResponseEntity.badRequest().body("Arquivo vazio.");
		if (!isValidImage(file.getContentType(), file.getOriginalFilename()))
			return ResponseEntity.badRequest().body("Somente JPEG e PNG são permitidos.");
		try {
			File dir = new File(UPLOAD_DIR + "/logo/cliente/" + id + "/");
			if (!dir.exists() && !dir.mkdirs())
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível criar o diretório.");
			java.io.File[] existing = dir.listFiles();
			if (existing != null) for (java.io.File fx : existing) fx.delete();
			BufferedImage original = ImageIO.read(file.getInputStream());
			if (original == null) return ResponseEntity.badRequest().body("Imagem inválida.");
			BufferedImage saida = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = saida.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(original, 0, 0, null);
			g.dispose();
			Path destino = Paths.get(dir.getAbsolutePath(), "logo_" + id + ".png");
			ImageIO.write(saida, "png", destino.toFile());
			return ResponseEntity.ok("Logo enviada com sucesso!");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a logo.");
		}
	}

	@GetMapping("/cliente/{id}/logo")
	public ResponseEntity<Resource> exibirLogoCliente(@PathVariable Long id) {
		Path arquivo = Paths.get(UPLOAD_DIR, "logo", "cliente", String.valueOf(id), "logo_" + id + ".png");
		if (!arquivo.toFile().exists()) return ResponseEntity.notFound().build();
		try {
			Resource recurso = new UrlResource(arquivo.toUri());
			return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_PNG)
				.header(HttpHeaders.CACHE_CONTROL, "no-cache")
				.body(recurso);
		} catch (MalformedURLException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	private boolean isValidUpload(String contentType, String fileName) {
	    return isPdf(contentType, fileName) || isJpeg(contentType, fileName) || isPng(contentType, fileName);
	}

	private boolean isPdf(String contentType, String fileName) {
	    boolean mimeOk = contentType != null && contentType.equalsIgnoreCase("application/pdf");
	    boolean extOk = fileName != null && fileName.toLowerCase().endsWith(".pdf");
	    return mimeOk || extOk;
	}

	private boolean isJpeg(String contentType, String fileName) {
	    boolean mimeOk = contentType != null && (
	            contentType.equalsIgnoreCase("image/jpeg") ||
	            contentType.equalsIgnoreCase("image/jpg")
	    );
	    boolean extOk = fileName != null && (
	            fileName.toLowerCase().endsWith(".jpg") ||
	            fileName.toLowerCase().endsWith(".jpeg")
	    );
	    return mimeOk || extOk;
	}

	private boolean isPng(String contentType, String fileName) {
	    boolean mimeOk = contentType != null && contentType.equalsIgnoreCase("image/png");
	    boolean extOk = fileName != null && fileName.toLowerCase().endsWith(".png");
	    return mimeOk || extOk;
	}



}
