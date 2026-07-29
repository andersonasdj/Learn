package br.com.techgold.learn.model;

import java.util.ArrayList;
import java.util.List;

import br.com.techgold.learn.dto.DtoAtualizarModeloCertificado;
import br.com.techgold.learn.dto.DtoCampoCertificado;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modelos_certificado")
@Getter
@Setter
@NoArgsConstructor
public class ModeloCertificado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String caminhoImagemFundo;
	private int larguraImagemPx;
	private int alturaImagemPx;
	@OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CampoCertificado> campos = new ArrayList<>();

	public void repopularCampos(List<DtoCampoCertificado> camposDados) {
		this.campos.clear();
		for (DtoCampoCertificado dados : camposDados) {
			this.campos.add(new CampoCertificado(dados, this));
		}
	}

	public void aplicar(DtoAtualizarModeloCertificado dados) {
		this.caminhoImagemFundo = dados.caminhoImagemFundo();
		this.larguraImagemPx = dados.larguraImagemPx();
		this.alturaImagemPx = dados.alturaImagemPx();
		this.repopularCampos(dados.campos());
	}
}
