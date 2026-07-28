package br.com.techgold.learn.model;

import br.com.techgold.learn.dto.DtoCadastroAlternativa;
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
@Table(name = "alternativas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alternativa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Questao questao;
	@Lob
	private String texto;
	private boolean correta;
	private int ordem;

	public Alternativa(DtoCadastroAlternativa dados, int ordem, Questao questao) {
		this.questao = questao;
		this.texto = dados.texto();
		this.correta = dados.correta();
		this.ordem = ordem;
	}
}
