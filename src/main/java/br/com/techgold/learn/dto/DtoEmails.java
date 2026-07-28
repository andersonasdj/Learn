package br.com.techgold.learn.dto;

import br.com.techgold.learn.model.enums.Agendamentos;

public record DtoEmails(
		Long id,
		Agendamentos agendamento,
		String email,
		boolean status
		) {

}
