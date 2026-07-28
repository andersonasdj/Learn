package br.com.techgold.learn.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "progresso_video", uniqueConstraints = @UniqueConstraint(columnNames = { "funcionario_id", "video_id" }))
@Getter
@Setter
@NoArgsConstructor
public class ProgressoVideo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private Video video;
	private int posicaoSegundos;
	private boolean concluido;
	private LocalDateTime dataAtualizacao;

}
