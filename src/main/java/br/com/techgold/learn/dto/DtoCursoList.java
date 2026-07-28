package br.com.techgold.learn.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.techgold.learn.model.Curso;
import br.com.techgold.learn.model.enums.CursoStatus;

public record DtoCursoList(
		Long id,
		String titulo,
		String caminhoCapa,
		CursoStatus status,
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime dataCriacao,
		String nomeAutor
		) implements Serializable {

	public DtoCursoList(Curso c) {
		this(c.getId(), c.getTitulo(), c.getCaminhoCapa(), c.getStatus(), c.getDataCriacao(),
				c.getAutor() != null ? c.getAutor().getNomeFuncionario() : null);
	}
}
