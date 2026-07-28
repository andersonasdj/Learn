package br.com.techgold.learn.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.techgold.learn.model.CursoColaboradorAcesso;

public record DtoAcessoColaboradorList(
		Long id,
		Long cursoId,
		Long colaboradorId,
		String nomeColaborador,
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime dataConcessao
		) {

	public DtoAcessoColaboradorList(CursoColaboradorAcesso a) {
		this(a.getId(), a.getCurso().getId(), a.getColaborador().getId(), a.getColaborador().getNomeColaborador(),
				a.getDataConcessao());
	}
}
