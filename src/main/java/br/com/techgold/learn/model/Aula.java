package br.com.techgold.learn.model;

import br.com.techgold.learn.dto.DtoAtualizarAula;
import br.com.techgold.learn.dto.DtoCadastroAula;
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
@Table(name = "aulas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aula {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Curso curso;
	private String titulo;
	private String descricao;
	private int ordem;

	public Aula(DtoCadastroAula dados, Curso curso) {
		this.curso = curso;
		this.titulo = dados.titulo();
		this.descricao = dados.descricao();
		this.ordem = dados.ordem();
	}

	public Aula(DtoAtualizarAula dados, Curso curso) {
		this.id = dados.id();
		this.curso = curso;
		this.titulo = dados.titulo();
		this.descricao = dados.descricao();
		this.ordem = dados.ordem();
	}
}
