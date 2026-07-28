package br.com.techgold.learn.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("curso")
@PreAuthorize("hasRole('ROLE_EDITOR')")
public class CursoController {

	@GetMapping("/list")
	public String listar() {
		return "cursoList.html";
	}

	@GetMapping("/form")
	public String formulario() {
		return "cursoForm.html";
	}

	@GetMapping("/gerenciar")
	public String gerenciar() {
		return "cursoGerenciar.html";
	}

	@GetMapping("/visualizar")
	public String visualizar() {
		return "cursoPreview.html";
	}

}
