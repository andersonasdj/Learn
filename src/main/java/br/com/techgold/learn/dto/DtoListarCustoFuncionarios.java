package br.com.techgold.learn.dto;

import java.math.BigDecimal;

import br.com.techgold.learn.model.Funcionario;

public record DtoListarCustoFuncionarios(
		Long id,
		BigDecimal valorHora) {
	
	public DtoListarCustoFuncionarios(Funcionario f) {
		this(
				f.getId(), 
				f.getValorHora()
				);
	}

}
