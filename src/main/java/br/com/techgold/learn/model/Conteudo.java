package br.com.techgold.learn.model;

import br.com.techgold.learn.dto.DtoCadastroConteudo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conteudos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Conteudo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Aula aula;
	private String titulo;
	@Lob
	private String corpoHtml;
	private int ordem;

	public Conteudo(DtoCadastroConteudo dados, Aula aula) {
		this.aula = aula;
		this.titulo = dados.titulo();
		this.corpoHtml = dados.corpoHtml();
		this.ordem = dados.ordem();
	}
}
