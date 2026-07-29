package br.com.techgold.learn.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.techgold.learn.dto.DtoAdesaoCurso;
import br.com.techgold.learn.dto.DtoAtualizarProgressoVideo;
import br.com.techgold.learn.dto.DtoAulaAdesao;
import br.com.techgold.learn.dto.DtoAulaProgresso;
import br.com.techgold.learn.dto.DtoCorrecaoQuestao;
import br.com.techgold.learn.dto.DtoCursoProgresso;
import br.com.techgold.learn.dto.DtoQuestaoAluno;
import br.com.techgold.learn.dto.DtoRespostaQuestao;
import br.com.techgold.learn.dto.DtoResponderTeste;
import br.com.techgold.learn.dto.DtoResultadoTeste;
import br.com.techgold.learn.dto.DtoTesteProgresso;
import br.com.techgold.learn.dto.DtoVideoProgresso;
import br.com.techgold.learn.model.Alternativa;
import br.com.techgold.learn.model.Aula;
import br.com.techgold.learn.model.Curso;
import br.com.techgold.learn.model.Funcionario;
import br.com.techgold.learn.model.ProgressoTeste;
import br.com.techgold.learn.model.ProgressoVideo;
import br.com.techgold.learn.model.Questao;
import br.com.techgold.learn.model.Teste;
import br.com.techgold.learn.model.Video;
import br.com.techgold.learn.repository.AulaRepository;
import br.com.techgold.learn.repository.CursoRepository;
import br.com.techgold.learn.repository.FuncionarioRepository;
import br.com.techgold.learn.repository.ProgressoTesteRepository;
import br.com.techgold.learn.repository.ProgressoVideoRepository;
import br.com.techgold.learn.repository.TesteRepository;
import br.com.techgold.learn.repository.VideoRepository;
import jakarta.transaction.Transactional;

@Service
public class ProgressoService {

	@Autowired private CursoRepository repositoryCurso;
	@Autowired private AulaRepository repositoryAula;
	@Autowired private VideoRepository repositoryVideo;
	@Autowired private TesteRepository repositoryTeste;
	@Autowired private FuncionarioRepository repositoryFuncionario;
	@Autowired private ProgressoVideoRepository repositoryProgressoVideo;
	@Autowired private ProgressoTesteRepository repositoryProgressoTeste;

	public boolean cursoConcluidoPeloFuncionarioLogado(Long cursoId) {
		DtoCursoProgresso arvore = obterProgresso(cursoId);
		return !arvore.aulas().isEmpty() && arvore.aulas().stream().allMatch(DtoAulaProgresso::concluida);
	}

	public DtoCursoProgresso obterProgresso(Long cursoId) {
		return construirArvore(funcionarioLogadoId(), cursoId);
	}

	private DtoCursoProgresso construirArvore(Long funcionarioId, Long cursoId) {
		Curso curso = repositoryCurso.getReferenceById(cursoId);
		List<Aula> aulas = repositoryAula.findByCursoIdOrderByOrdemAsc(cursoId);

		Map<Long, ProgressoVideo> progressoVideos = repositoryProgressoVideo
				.buscarPorFuncionarioECurso(funcionarioId, cursoId).stream()
				.collect(Collectors.toMap(p -> p.getVideo().getId(), Function.identity()));
		Map<Long, ProgressoTeste> progressoTestes = repositoryProgressoTeste
				.buscarPorFuncionarioECurso(funcionarioId, cursoId).stream()
				.collect(Collectors.toMap(p -> p.getTeste().getId(), Function.identity()));

		List<DtoAulaProgresso> aulasDto = new ArrayList<>();
		boolean aulaAnteriorConcluida = true;

		for (Aula aula : aulas) {
			boolean aulaLiberada = aulaAnteriorConcluida;
			List<Video> videos = repositoryVideo.findByAulaIdOrderByOrdemAsc(aula.getId());

			List<DtoVideoProgresso> videosDto = new ArrayList<>();
			boolean videoAnteriorConcluido = true;
			for (Video v : videos) {
				ProgressoVideo pv = progressoVideos.get(v.getId());
				boolean concluido = pv != null && pv.isConcluido();
				int posicao = pv != null ? pv.getPosicaoSegundos() : 0;
				boolean liberado = aulaLiberada && videoAnteriorConcluido;
				videosDto.add(new DtoVideoProgresso(v.getId(), v.getTitulo(), v.getUrl(), v.getDuracaoSegundos(),
						v.getOrdem(), liberado, concluido, posicao));
				videoAnteriorConcluido = concluido;
			}
			boolean todosVideosConcluidos = videosDto.stream().allMatch(DtoVideoProgresso::concluido);

			Teste teste = repositoryTeste.findByAulaId(aula.getId()).orElse(null);
			DtoTesteProgresso testeDto = null;
			boolean aulaConcluida;

			if (teste != null) {
				ProgressoTeste pt = progressoTestes.get(teste.getId());
				boolean aprovado = pt != null && pt.isAprovado();
				int tentativas = pt != null ? pt.getTentativas() : 0;
				int tentativasMaximas = limiteTentativas(teste);
				boolean bloqueado = !aprovado && tentativas >= tentativasMaximas;
				boolean testeLiberado = aulaLiberada && todosVideosConcluidos;
				List<DtoQuestaoAluno> questoesDto = teste.getQuestoes().stream()
						.sorted(Comparator.comparingInt(Questao::getOrdem))
						.map(DtoQuestaoAluno::new)
						.toList();
				testeDto = new DtoTesteProgresso(teste.getId(), teste.getTitulo(), teste.getNotaAprovacao(),
						tentativasMaximas, testeLiberado, aprovado, tentativas,
						pt != null ? pt.getNotaObtida() : null, bloqueado, questoesDto);
				aulaConcluida = aprovado;
			} else {
				aulaConcluida = todosVideosConcluidos;
			}

			aulasDto.add(new DtoAulaProgresso(aula.getId(), aula.getTitulo(), aula.getOrdem(), aulaLiberada,
					aulaConcluida, videosDto, testeDto));
			aulaAnteriorConcluida = aulaConcluida;
		}

		return new DtoCursoProgresso(cursoId, curso.getTitulo(), aulasDto);
	}

	public DtoAdesaoCurso obterAdesao(Long cursoId) {
		Curso curso = repositoryCurso.getReferenceById(cursoId);

		Set<Long> funcionarioIds = new HashSet<>();
		funcionarioIds.addAll(repositoryProgressoVideo.buscarFuncionarioIdsPorCurso(cursoId));
		funcionarioIds.addAll(repositoryProgressoTeste.buscarFuncionarioIdsPorCurso(cursoId));

		List<Aula> aulas = repositoryAula.findByCursoIdOrderByOrdemAsc(cursoId);
		Map<Long, int[]> contagemPorAula = new LinkedHashMap<>();
		aulas.forEach(a -> contagemPorAula.put(a.getId(), new int[2])); // [0]=concluidos, [1]=emAndamento

		for (Long funcionarioId : funcionarioIds) {
			DtoCursoProgresso arvore = construirArvore(funcionarioId, cursoId);
			for (DtoAulaProgresso aula : arvore.aulas()) {
				int[] contagem = contagemPorAula.get(aula.id());
				if (aula.concluida()) {
					contagem[0]++;
				} else {
					boolean comAlgumProgresso = aula.videos().stream().anyMatch(DtoVideoProgresso::concluido)
							|| (aula.teste() != null && aula.teste().tentativas() > 0);
					if (comAlgumProgresso) contagem[1]++;
				}
			}
		}

		int totalAlunos = funcionarioIds.size();
		List<DtoAulaAdesao> aulasAdesao = aulas.stream().map(a -> {
			int[] c = contagemPorAula.get(a.getId());
			int naoIniciados = Math.max(0, totalAlunos - c[0] - c[1]);
			return new DtoAulaAdesao(a.getId(), a.getTitulo(), a.getOrdem(), c[0], c[1], naoIniciados);
		}).toList();

		return new DtoAdesaoCurso(cursoId, curso.getTitulo(), totalAlunos, aulasAdesao);
	}

	@Transactional
	public void salvarProgressoVideo(Long videoId, DtoAtualizarProgressoVideo dados) {
		Video video = repositoryVideo.findById(videoId)
				.orElseThrow(() -> new IllegalArgumentException("Vídeo não encontrado"));
		Long cursoId = video.getAula().getCurso().getId();

		DtoVideoProgresso estado = localizarVideo(obterProgresso(cursoId), videoId);
		if (estado == null || !estado.liberado()) {
			throw new IllegalStateException("Este vídeo ainda não foi liberado");
		}

		Long funcionarioId = funcionarioLogadoId();
		ProgressoVideo progresso = repositoryProgressoVideo.findByFuncionarioIdAndVideoId(funcionarioId, videoId)
				.orElseGet(() -> {
					ProgressoVideo novo = new ProgressoVideo();
					novo.setFuncionario(repositoryFuncionario.getReferenceById(funcionarioId));
					novo.setVideo(video);
					return novo;
				});

		progresso.setPosicaoSegundos(dados.posicaoSegundos());
		progresso.setConcluido(progresso.isConcluido() || dados.concluido());
		progresso.setDataAtualizacao(LocalDateTime.now().withNano(0));
		repositoryProgressoVideo.save(progresso);
	}

	@Transactional
	public DtoResultadoTeste responderTeste(Long testeId, DtoResponderTeste dados) {
		Teste teste = repositoryTeste.findById(testeId)
				.orElseThrow(() -> new IllegalArgumentException("Teste não encontrado"));
		Long cursoId = teste.getAula().getCurso().getId();

		DtoTesteProgresso estado = localizarTeste(obterProgresso(cursoId), testeId);
		if (estado == null || !estado.liberado()) {
			throw new IllegalStateException("Este teste ainda não foi liberado");
		}
		if (estado.bloqueado()) {
			throw new IllegalStateException("Número máximo de tentativas atingido");
		}

		List<Questao> questoes = teste.getQuestoes();
		Map<Long, Long> respostasPorQuestao = dados.respostas().stream()
				.collect(Collectors.toMap(DtoRespostaQuestao::questaoId, DtoRespostaQuestao::alternativaId));

		if (respostasPorQuestao.size() < questoes.size()) {
			throw new IllegalArgumentException("Responda todas as questões antes de enviar");
		}

		List<DtoCorrecaoQuestao> correcoes = new ArrayList<>();
		int acertos = 0;
		for (Questao q : questoes) {
			Long alternativaEscolhidaId = respostasPorQuestao.get(q.getId());
			Alternativa correta = q.getAlternativas().stream().filter(Alternativa::isCorreta).findFirst().orElse(null);
			boolean acertou = correta != null && correta.getId().equals(alternativaEscolhidaId);
			if (acertou) acertos++;
			correcoes.add(new DtoCorrecaoQuestao(q.getId(), correta != null ? correta.getId() : null,
					alternativaEscolhidaId, acertou));
		}

		int total = questoes.size();
		int percentual = total > 0 ? Math.round(acertos * 100f / total) : 0;
		boolean aprovadoNestaTentativa = percentual >= teste.getNotaAprovacao();

		Long funcionarioId = funcionarioLogadoId();
		ProgressoTeste progresso = repositoryProgressoTeste.findByFuncionarioIdAndTesteId(funcionarioId, testeId)
				.orElseGet(() -> {
					ProgressoTeste novo = new ProgressoTeste();
					novo.setFuncionario(repositoryFuncionario.getReferenceById(funcionarioId));
					novo.setTeste(teste);
					novo.setTentativas(0);
					novo.setAprovado(false);
					return novo;
				});

		progresso.setTentativas(progresso.getTentativas() + 1);
		int melhorNota = Math.max(percentual, progresso.getNotaObtida() != null ? progresso.getNotaObtida() : 0);
		progresso.setNotaObtida(melhorNota);
		if (!progresso.isAprovado() && aprovadoNestaTentativa) {
			progresso.setAprovado(true);
			progresso.setDataConclusao(LocalDateTime.now().withNano(0));
		}
		repositoryProgressoTeste.save(progresso);

		int tentativasMaximas = limiteTentativas(teste);
		int tentativasRestantes = Math.max(0, tentativasMaximas - progresso.getTentativas());
		boolean bloqueadoAgora = !progresso.isAprovado() && progresso.getTentativas() >= tentativasMaximas;

		return new DtoResultadoTeste(acertos, total, percentual, progresso.isAprovado(), progresso.getTentativas(),
				tentativasRestantes, bloqueadoAgora, correcoes);
	}

	@Transactional
	public void reiniciarProgresso(Long cursoId) {
		Long funcionarioId = funcionarioLogadoId();
		repositoryProgressoVideo.excluirPorFuncionarioECurso(funcionarioId, cursoId);
		repositoryProgressoTeste.excluirPorFuncionarioECurso(funcionarioId, cursoId);
	}

	private DtoVideoProgresso localizarVideo(DtoCursoProgresso arvore, Long videoId) {
		return arvore.aulas().stream()
				.flatMap(a -> a.videos().stream())
				.filter(v -> v.id().equals(videoId))
				.findFirst()
				.orElse(null);
	}

	private DtoTesteProgresso localizarTeste(DtoCursoProgresso arvore, Long testeId) {
		return arvore.aulas().stream()
				.map(DtoAulaProgresso::teste)
				.filter(t -> t != null && t.id().equals(testeId))
				.findFirst()
				.orElse(null);
	}

	private int limiteTentativas(Teste teste) {
		return teste.getTentativasMaximas() > 0 ? teste.getTentativasMaximas() : 3;
	}

	public Long funcionarioLogadoId() {
		String nomeFuncionario = ((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getNomeFuncionario();
		return repositoryFuncionario.findBynomeFuncionario(nomeFuncionario).getId();
	}

}
