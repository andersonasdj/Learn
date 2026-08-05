package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.Colaborador;

public record DtoColaboradorEdit(
		Long id,
		String nomeColaborador,
		String celular,
		String email,
		Boolean ativo,
		Long idCliente,
		String username,
		String password
		) {

	public DtoColaboradorEdit(Colaborador c) {
		this(c.getId(), c.getNomeColaborador(), c.getCelular(), c.getEmail(), c.getAtivo(), c.getCliente().getId(), c.getUsername(), c.getPassword());

	}


}
