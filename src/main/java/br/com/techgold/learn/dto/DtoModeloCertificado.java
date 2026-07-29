package br.com.techgold.learn.dto;

import java.util.List;

import br.com.techgold.learn.model.ModeloCertificado;

public record DtoModeloCertificado(
		Long id,
		String caminhoImagemFundo,
		int larguraImagemPx,
		int alturaImagemPx,
		List<DtoCampoCertificado> campos
		) {

	public DtoModeloCertificado(ModeloCertificado m) {
		this(m.getId(), m.getCaminhoImagemFundo(), m.getLarguraImagemPx(), m.getAlturaImagemPx(),
				m.getCampos().stream().map(DtoCampoCertificado::new).toList());
	}
}
