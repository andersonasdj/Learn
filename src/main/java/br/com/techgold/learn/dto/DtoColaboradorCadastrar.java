package br.com.techgold.learn.dto;

public record DtoColaboradorCadastrar(
		String nomeColaborador,
		String celular,
		boolean vip,
		Long clienteId,
		String email,
		String username,
		String password
		) {

}
