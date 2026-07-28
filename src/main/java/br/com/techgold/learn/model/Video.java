package br.com.techgold.learn.model;

import br.com.techgold.learn.dto.DtoCadastroVideo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "videos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Video {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Aula aula;
	private String titulo;
	private String url;
	private String nomeArquivoOriginal;
	private Integer duracaoSegundos;
	private int ordem;

	public Video(DtoCadastroVideo dados, Aula aula, String caminhoArquivo, String nomeArquivoOriginal) {
		this.aula = aula;
		this.titulo = dados.titulo();
		this.url = caminhoArquivo;
		this.nomeArquivoOriginal = nomeArquivoOriginal;
		this.duracaoSegundos = dados.duracaoSegundos();
		this.ordem = dados.ordem();
	}
}
