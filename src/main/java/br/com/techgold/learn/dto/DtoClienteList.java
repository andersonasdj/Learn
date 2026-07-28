package br.com.techgold.learn.dto;

import java.io.Serializable;

import br.com.techgold.learn.model.Cliente;

public record DtoClienteList(
		Long id,
		boolean ativo,
		String nomeCliente,
		boolean vip,
		boolean redFlag,
		Long tempoContratado
		
		) implements Serializable {
	
	public DtoClienteList(Cliente c){
		this(c.getId(), c.getAtivo(), c.getNomeCliente(), c.isVip(), c.isRedFlag(), c.getTempoContratado());
		
		
	}

}
