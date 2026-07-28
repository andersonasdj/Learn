package br.com.techgold.learn.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "curso_cliente_acesso", uniqueConstraints = @UniqueConstraint(columnNames = { "curso_id", "cliente_id" }))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CursoClienteAcesso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Curso curso;
	@ManyToOne
	private Cliente cliente;
	private LocalDateTime dataConcessao;
	@ManyToOne
	private Funcionario concedidoPor;

	public CursoClienteAcesso(Curso curso, Cliente cliente, Funcionario concedidoPor) {
		this.curso = curso;
		this.cliente = cliente;
		this.concedidoPor = concedidoPor;
		this.dataConcessao = LocalDateTime.now().withNano(0);
	}
}
