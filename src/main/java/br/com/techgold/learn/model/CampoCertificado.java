package br.com.techgold.learn.model;

import br.com.techgold.learn.dto.DtoCampoCertificado;
import br.com.techgold.learn.model.enums.AlinhamentoTexto;
import br.com.techgold.learn.model.enums.CampoCertificadoTipo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "campos_certificado")
@Getter
@Setter
@NoArgsConstructor
public class CampoCertificado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private ModeloCertificado modelo;
	@Enumerated(EnumType.STRING)
	private CampoCertificadoTipo tipoCampo;
	private double posXPercentual;
	private double posYPercentual;
	private int tamanhoFonte;
	private String corHex;
	private boolean negrito;
	@Enumerated(EnumType.STRING)
	private AlinhamentoTexto alinhamento;

	public CampoCertificado(DtoCampoCertificado dados, ModeloCertificado modelo) {
		this.modelo = modelo;
		this.tipoCampo = dados.tipoCampo();
		this.posXPercentual = dados.posXPercentual();
		this.posYPercentual = dados.posYPercentual();
		this.tamanhoFonte = dados.tamanhoFonte();
		this.corHex = dados.corHex();
		this.negrito = dados.negrito();
		this.alinhamento = dados.alinhamento();
	}
}
