package br.com.techgold.learn.model;

import java.util.ArrayList;
import java.util.List;

import br.com.techgold.learn.dto.DtoCadastroQuestao;
import br.com.techgold.learn.dto.DtoCadastroTeste;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "testes")
@Getter
@Setter
@NoArgsConstructor
public class Teste {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name = "aula_id", unique = true)
	private Aula aula;
	private String titulo;
	private int notaAprovacao;
	private int tentativasMaximas;
	@OneToMany(mappedBy = "teste", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Questao> questoes = new ArrayList<>();

	public Teste(DtoCadastroTeste dados, Aula aula) {
		this.aula = aula;
		this.titulo = dados.titulo();
		this.notaAprovacao = dados.notaAprovacao();
		this.tentativasMaximas = dados.tentativasMaximas();
		this.questoes = new ArrayList<>();
		this.repopularQuestoes(dados.questoes());
	}

	public void repopularQuestoes(List<DtoCadastroQuestao> questoesDados) {
		this.questoes.clear();
		for (int i = 0; i < questoesDados.size(); i++) {
			this.questoes.add(new Questao(questoesDados.get(i), i, this));
		}
	}
}
