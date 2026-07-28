package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.UserRole;
import jakarta.validation.constraints.NotBlank;

public record DtoCadastroFuncionario(
		
		@NotBlank
		String nomeFuncionario,
		@NotBlank
		String username,
		@NotBlank
		String password,
		UserRole role) {

}
