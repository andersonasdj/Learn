package br.com.techgold.learn.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.techgold.learn.model.Certificado;

public record DtoCertificado(
		Long id,
		Long cursoId,
		String nomeCurso,
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime dataEmissao,
		String codigoValidacao,
		double cargaHorariaHorasNaEmissao
		) {

	public DtoCertificado(Certificado c) {
		this(c.getId(), c.getCurso().getId(), c.getCurso().getTitulo(), c.getDataEmissao(),
				c.getCodigoValidacao(), c.getCargaHorariaHorasNaEmissao());
	}
}
