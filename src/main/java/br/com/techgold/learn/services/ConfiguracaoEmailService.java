package br.com.techgold.learn.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgold.learn.dto.DtoEmails;
import br.com.techgold.learn.model.ConfiguracaoEmail;
import br.com.techgold.learn.repository.ConfiguracaoEmailRepository;
import jakarta.transaction.Transactional;

@Service
public class ConfiguracaoEmailService {

	@Autowired ConfiguracaoEmailRepository repository;
	
	public void cadastra(ConfiguracaoEmail email) {
		repository.save(email);
	}
	
	public int existeEmail() {
		return repository.existsConfigEmails();
	}

	public List<ConfiguracaoEmail> listarEmails() {
		
		return repository.listarEmails();
	}

	@Transactional
	public void atualiza(List<DtoEmails> dados) {
		
		dados.forEach(d -> {
			repository.save(new ConfiguracaoEmail(d));
		});
		
	}
	
	public ConfiguracaoEmail buscaConfiguracao(String agendamentos) {
		return repository.buscaPorConfiguracao(agendamentos);
	}
	
}
