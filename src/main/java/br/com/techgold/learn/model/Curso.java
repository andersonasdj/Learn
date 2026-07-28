package br.com.techgold.learn.model;

import java.time.LocalDateTime;

import br.com.techgold.learn.dto.DtoAtualizarCurso;
import br.com.techgold.learn.dto.DtoCadastroCurso;
import br.com.techgold.learn.model.enums.CursoStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "cursos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String descricao;
	private String caminhoCapa;
	@Enumerated(EnumType.STRING)
	private CursoStatus status;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;
	@ManyToOne
	private Funcionario autor;

	public Curso(DtoCadastroCurso dados, Funcionario autor) {
		this.titulo = dados.titulo();
		this.descricao = dados.descricao();
		this.caminhoCapa = dados.caminhoCapa();
		this.status = CursoStatus.RASCUNHO;
		this.dataCriacao = LocalDateTime.now().withNano(0);
		this.dataAtualizacao = this.dataCriacao;
		this.autor = autor;
	}

	public Curso(DtoAtualizarCurso dados, Funcionario autor) {
		this.id = dados.id();
		this.titulo = dados.titulo();
		this.descricao = dados.descricao();
		this.caminhoCapa = dados.caminhoCapa();
		this.status = dados.status();
		this.dataAtualizacao = LocalDateTime.now().withNano(0);
		this.autor = autor;
	}
}
