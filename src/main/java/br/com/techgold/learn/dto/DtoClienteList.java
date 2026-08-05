package br.com.techgold.learn.dto;

import java.io.Serializable;

import br.com.techgold.learn.model.Cliente;

public record DtoClienteList(
		Long id,
		boolean ativo,
		String nomeCliente

		) implements Serializable {

	public DtoClienteList(Cliente c){
		this(c.getId(), c.getAtivo(), c.getNomeCliente());


	}

}
