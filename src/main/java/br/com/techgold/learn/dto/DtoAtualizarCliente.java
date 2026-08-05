package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoAtualizarCliente(
		@NotNull
		Long id,
		@NotBlank
		String nomeCliente,
		@NotBlank
		String username,
		@NotBlank
		String password,
		String endereco,
		String telefone,
		String cnpj,
		Boolean ativo,
		String bairro) {

	public DtoAtualizarCliente(Cliente c) {

		this(c.getId(),c.getNomeCliente(),c.getUsername(),c.getPassword(),c.getEndereco(),c.getTelefone(),c.getCnpj(),c.getAtivo(), c.getBairro());
	}

}
