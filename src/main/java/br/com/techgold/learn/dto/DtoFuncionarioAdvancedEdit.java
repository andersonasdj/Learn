package br.com.techgold.learn.dto;

import java.math.BigDecimal;

import br.com.techgold.learn.model.Funcionario;
import br.com.techgold.learn.model.UserRole;

public record DtoFuncionarioAdvancedEdit(
		Long id,
		UserRole role,
		BigDecimal valorHora
		) {
	
	public DtoFuncionarioAdvancedEdit(Funcionario f) {
		this(
				f.getId(), 
				f.getRole(),
				f.getValorHora()
				);
	}

}
