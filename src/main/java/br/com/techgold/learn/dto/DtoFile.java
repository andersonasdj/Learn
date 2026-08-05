package br.com.techgold.learn.dto;

import org.springframework.web.multipart.MultipartFile;

public record DtoFile(
		MultipartFile file,
		Long id
		) {

}
