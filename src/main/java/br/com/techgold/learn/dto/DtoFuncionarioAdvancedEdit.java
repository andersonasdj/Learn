package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.Funcionario;
import br.com.techgold.learn.model.UserRole;

public record DtoFuncionarioAdvancedEdit(
		Long id,
		UserRole role
		) {

	public DtoFuncionarioAdvancedEdit(Funcionario f) {
		this(
				f.getId(),
				f.getRole()
				);
	}

}
