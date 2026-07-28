package br.com.techgold.learn.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.techgold.learn.model.CursoClienteAcesso;

public record DtoAcessoClienteList(
		Long id,
		Long cursoId,
		Long clienteId,
		String nomeCliente,
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime dataConcessao
		) {

	public DtoAcessoClienteList(CursoClienteAcesso a) {
		this(a.getId(), a.getCurso().getId(), a.getCliente().getId(), a.getCliente().getNomeCliente(),
				a.getDataConcessao());
	}
}
