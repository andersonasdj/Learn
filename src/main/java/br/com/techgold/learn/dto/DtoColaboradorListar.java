package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.Colaborador;

public record DtoColaboradorListar(
		Long id,
		String nomeColaborador,
		String celular,
		Long clienteId,
		String email,
		String usermail

		) {

	public DtoColaboradorListar(Colaborador c) {
		this(c.getId(), c.getNomeColaborador(), c.getCelular(), c.getId(), c.getEmail(), c.getUsername());
	}

}
