package br.com.techgold.learn.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.techgold.learn.model.Certificado;

public record DtoCertificadoEmitido(
		Long id,
		String nomeFuncionario,
		Long cursoId,
		String nomeCurso,
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime dataEmissao,
		String codigoValidacao,
		double cargaHorariaHorasNaEmissao
		) {

	public DtoCertificadoEmitido(Certificado c) {
		this(c.getId(), c.getFuncionario().getNomeFuncionario(), c.getCurso().getId(), c.getCurso().getTitulo(),
				c.getDataEmissao(), c.getCodigoValidacao(), c.getCargaHorariaHorasNaEmissao());
	}
}
