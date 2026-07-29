package br.com.techgold.learn.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "certificados", uniqueConstraints = @UniqueConstraint(columnNames = { "funcionario_id", "curso_id" }))
@Getter
@Setter
@NoArgsConstructor
public class Certificado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private Curso curso;
	private LocalDateTime dataEmissao;
	private String codigoValidacao;
	private double cargaHorariaHorasNaEmissao;

	public Certificado(Funcionario funcionario, Curso curso, double cargaHorariaHoras) {
		this.funcionario = funcionario;
		this.curso = curso;
		this.cargaHorariaHorasNaEmissao = cargaHorariaHoras;
		this.dataEmissao = LocalDateTime.now().withNano(0);
		this.codigoValidacao = UUID.randomUUID().toString();
	}
}
