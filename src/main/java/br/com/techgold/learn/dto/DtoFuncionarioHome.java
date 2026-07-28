package br.com.techgold.learn.dto;

public record DtoFuncionarioHome(
		String nomeFuncionario,
		String dataUltimoLogin,
		Boolean trocaSenha,
		Long id
		) {
}
