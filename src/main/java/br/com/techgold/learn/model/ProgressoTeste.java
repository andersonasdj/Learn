package br.com.techgold.learn.model;

import java.time.LocalDateTime;

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
@Table(name = "progresso_teste", uniqueConstraints = @UniqueConstraint(columnNames = { "funcionario_id", "teste_id" }))
@Getter
@Setter
@NoArgsConstructor
public class ProgressoTeste {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private Teste teste;
	private boolean aprovado;
	private Integer notaObtida;
	private int tentativas;
	private LocalDateTime dataConclusao;

}
