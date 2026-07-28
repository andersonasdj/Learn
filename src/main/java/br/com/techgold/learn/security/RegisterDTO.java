package br.com.techgold.learn.security;

public record RegisterDTO(
		String username,
		String password,
		String role
		) {

}
