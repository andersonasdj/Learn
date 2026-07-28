package br.com.techgold.learn.dto;

public record DtoFuncionarioDisponibilidade(
		Long id,
		String nomeFuncionario,
		Boolean ausente,
		Boolean refeicao,
		Boolean comAndamento
) {}
