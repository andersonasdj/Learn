package br.com.techgold.learn.model;

import java.util.ArrayList;
import java.util.List;

import br.com.techgold.learn.dto.DtoCadastroAlternativa;
import br.com.techgold.learn.dto.DtoCadastroQuestao;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "questoes")
@Getter
@Setter
@NoArgsConstructor
public class Questao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Teste teste;
	@Lob
	private String enunciado;
	private int ordem;
	@OneToMany(mappedBy = "questao", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Alternativa> alternativas = new ArrayList<>();

	public Questao(DtoCadastroQuestao dados, int ordem, Teste teste) {
		this.teste = teste;
		this.enunciado = dados.enunciado();
		this.ordem = ordem;
		this.alternativas = new ArrayList<>();
		List<DtoCadastroAlternativa> alternativasDados = dados.alternativas();
		for (int i = 0; i < alternativasDados.size(); i++) {
			this.alternativas.add(new Alternativa(alternativasDados.get(i), i, this));
		}
	}
}
