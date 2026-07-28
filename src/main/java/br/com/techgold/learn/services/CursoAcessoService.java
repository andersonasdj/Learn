package br.com.techgold.learn.services;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgold.learn.dto.DtoAcessoClienteList;
import br.com.techgold.learn.dto.DtoAcessoColaboradorList;
import br.com.techgold.learn.dto.DtoCadastroCursoClienteAcesso;
import br.com.techgold.learn.dto.DtoCadastroCursoColaboradorAcesso;
import br.com.techgold.learn.dto.DtoCursoList;
import br.com.techgold.learn.model.Cliente;
import br.com.techgold.learn.model.Colaborador;
import br.com.techgold.learn.model.Curso;
import br.com.techgold.learn.model.CursoClienteAcesso;
import br.com.techgold.learn.model.CursoColaboradorAcesso;
import br.com.techgold.learn.model.Funcionario;
import br.com.techgold.learn.repository.ClienteRepository;
import br.com.techgold.learn.repository.ColaboradorRepository;
import br.com.techgold.learn.repository.CursoClienteAcessoRepository;
import br.com.techgold.learn.repository.CursoColaboradorAcessoRepository;
import br.com.techgold.learn.repository.CursoRepository;
import br.com.techgold.learn.repository.FuncionarioRepository;

@Service
public class CursoAcessoService {

	@Autowired private CursoClienteAcessoRepository repositoryClienteAcesso;
	@Autowired private CursoColaboradorAcessoRepository repositoryColaboradorAcesso;
	@Autowired private CursoRepository repositoryCurso;
	@Autowired private ClienteRepository repositoryCliente;
	@Autowired private ColaboradorRepository repositoryColaborador;
	@Autowired private FuncionarioRepository repositoryFuncionario;

	public void liberarParaCliente(DtoCadastroCursoClienteAcesso dados) {
		if (repositoryClienteAcesso.existsByCursoIdAndClienteId(dados.cursoId(), dados.clienteId())) {
			throw new IllegalStateException("Este cliente já tem acesso a este curso");
		}
		Curso curso = repositoryCurso.getReferenceById(dados.cursoId());
		Cliente cliente = repositoryCliente.getReferenceById(dados.clienteId());
		Funcionario concedidoPor = repositoryFuncionario.getReferenceById(dados.concedidoPorId());
		repositoryClienteAcesso.save(new CursoClienteAcesso(curso, cliente, concedidoPor));
	}

	public void revogarDeCliente(Long id) {
		repositoryClienteAcesso.deleteById(id);
	}

	public List<DtoAcessoClienteList> listarClientesComAcesso(Long cursoId) {
		return repositoryClienteAcesso.findByCursoId(cursoId).stream().map(DtoAcessoClienteList::new).toList();
	}

	public void liberarParaColaborador(DtoCadastroCursoColaboradorAcesso dados) {
		if (repositoryColaboradorAcesso.existsByCursoIdAndColaboradorId(dados.cursoId(), dados.colaboradorId())) {
			throw new IllegalStateException("Este colaborador já tem acesso a este curso");
		}
		Curso curso = repositoryCurso.getReferenceById(dados.cursoId());
		Colaborador colaborador = repositoryColaborador.getReferenceById(dados.colaboradorId());
		Funcionario concedidoPor = repositoryFuncionario.getReferenceById(dados.concedidoPorId());
		repositoryColaboradorAcesso.save(new CursoColaboradorAcesso(curso, colaborador, concedidoPor));
	}

	public void revogarDeColaborador(Long id) {
		repositoryColaboradorAcesso.deleteById(id);
	}

	public List<DtoAcessoColaboradorList> listarColaboradoresComAcesso(Long cursoId) {
		return repositoryColaboradorAcesso.findByCursoId(cursoId).stream().map(DtoAcessoColaboradorList::new).toList();
	}

	public boolean temAcesso(Long colaboradorId, Long cursoId) {
		if (repositoryColaboradorAcesso.existsByCursoIdAndColaboradorId(cursoId, colaboradorId)) {
			return true;
		}
		Long clienteId = repositoryColaborador.buscaClienteIdPorId(colaboradorId);
		return clienteId != null && repositoryClienteAcesso.existsByCursoIdAndClienteId(cursoId, clienteId);
	}

	public List<DtoCursoList> listarCursosAcessiveis(Long colaboradorId) {
		List<Long> idsPorColaborador = repositoryColaboradorAcesso.buscarCursoIdsPorColaboradorId(colaboradorId);
		Long clienteId = repositoryColaborador.buscaClienteIdPorId(colaboradorId);
		List<Long> idsPorCliente = clienteId != null
				? repositoryClienteAcesso.buscarCursoIdsPorClienteId(clienteId)
				: List.of();
		List<Long> idsAcessiveis = Stream.concat(idsPorColaborador.stream(), idsPorCliente.stream()).distinct().toList();
		return repositoryCurso.findAllById(idsAcessiveis).stream().map(DtoCursoList::new).toList();
	}

}
