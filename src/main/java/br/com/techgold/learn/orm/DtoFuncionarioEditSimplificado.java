package br.com.techgold.learn.orm;

import java.time.LocalDateTime;

public interface DtoFuncionarioEditSimplificado {

	Long getId(); 
	String getNomeFuncionario(); 
	String getUsername(); 
	LocalDateTime getDataAtualizacao();

}
