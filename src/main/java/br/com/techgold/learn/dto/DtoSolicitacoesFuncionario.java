package br.com.techgold.learn.dto;

public record DtoSolicitacoesFuncionario(
		int abertas,
		int andamento,
		int agendados,
		int aguardando,
		int pausado,
		int total) {

}
