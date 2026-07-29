package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.CampoCertificado;
import br.com.techgold.learn.model.enums.AlinhamentoTexto;
import br.com.techgold.learn.model.enums.CampoCertificadoTipo;
import jakarta.validation.constraints.NotNull;

public record DtoCampoCertificado(
		@NotNull
		CampoCertificadoTipo tipoCampo,
		double posXPercentual,
		double posYPercentual,
		int tamanhoFonte,
		String corHex,
		boolean negrito,
		@NotNull
		AlinhamentoTexto alinhamento
		) {

	public DtoCampoCertificado(CampoCertificado c) {
		this(c.getTipoCampo(), c.getPosXPercentual(), c.getPosYPercentual(), c.getTamanhoFonte(),
				c.getCorHex(), c.isNegrito(), c.getAlinhamento());
	}
}
