package br.com.techgold.learn.dto;

import java.io.Serializable;

import br.com.techgold.learn.model.Funcionario;

public record DtoListarFuncionarios(

		Long id,
		String nomeFuncionario,
		String username,
		Boolean ativo,
		Boolean mfa,
		String role,
		String dataAtualizacao,
		String email
		) implements Serializable {


	public DtoListarFuncionarios(Funcionario f) {
		this(f.getId(), f.getNomeFuncionario(), f.getUsername(), f.getAtivo(), f.getMfa(), f.getRole().toString(), f.getDataAtualizacao().toString(), f.getEmail());
	}


}
