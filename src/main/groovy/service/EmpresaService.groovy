package service

import DAO.CandidatoDAO
import DAO.EmpresaDAO
import DAO.VagaDAO
import model.Candidato
import model.Empresa
import model.Vaga

class EmpresaService {
    private final EmpresaDAO empresaDAO
    private final VagaDAO vagaDAO
    private final CandidatoDAO candidatoDAO

    EmpresaService(EmpresaDAO eDAO, VagaDAO vDAO, CandidatoDAO cDAO) {
        this.empresaDAO = eDAO
        this.vagaDAO = vDAO
        this.candidatoDAO = cDAO
    }

    void registrarEmpresa(Map d) {
        Empresa e = new Empresa(
                (d.nome ?: "").toString(),
                (d.email ?: "").toString(),
                (d.pais ?: "").toString(),
                (d.cep ?: "").toString(),
                (d.desc ?: "").toString(),
                (d.cnpj ?: "").toString()
        )
        e.senha = (d.senha ?: "").toString()
        empresaDAO.salvar(e)
    }

    void anunciarVaga(int empresaId, String titulo, String desc, String local, List<String> reqs) {
        Empresa emp = new Empresa()
        emp.id = empresaId

        Vaga v = new Vaga(titulo, desc, reqs, emp)
        v.localEstadoCidade = local

        int idVaga = vagaDAO.salvar(v)

        if (idVaga > 0 && reqs) {
            vagaDAO.atualizarCompetenciasNoBanco(idVaga, reqs)
        }
    }

    List<Vaga> listarVagasPorEmpresa(int empresaId) {
        List<Vaga> todasAsVagas = vagaDAO.listarTodos() ?: []
        return todasAsVagas.findAll { it?.empresa?.id == empresaId }
    }

    void atualizarVagaCompleta(int vagaId, Map dados) {
        vagaDAO.atualizarBasico(vagaId, (String) dados.nome, (String) dados.descricao)

        if (dados.requisitos != null) {
            List<String> novosRequisitos = dados.requisitos.toString().split(',')
                    .collect { it.trim() }
                    .findAll { !it.isEmpty() }
                    .unique()

            vagaDAO.atualizarCompetenciasNoBanco(vagaId, novosRequisitos)
        }
    }

    void excluirVaga(int id) {
        vagaDAO.deletar(id)
    }

    Map gerenciarInteresse(int empId, int candId) {
        empresaDAO.registrarCurtida(empId, candId)
        List<Map> matches = empresaDAO.listarMatchesReais(empId)
        return matches.find { it.candidatoid == candId }
    }

    List<Map> listarCandidatosQueCurtiuEmpresa(int empId) {
        return empresaDAO.listarCandidatosInteressados(empId) ?: []
    }
    List<Candidato> obterMatchesCompletos(int empresaId) {
        List<Integer> ids = empresaDAO.listarIdsCandidatosComMatch(empresaId)
        List<Candidato> todosCandidatos = candidatoDAO.listarTodos()

        return todosCandidatos.findAll { cand -> ids.contains(cand.id) }
    }

    List<Empresa> obterTodas() {
        return empresaDAO.listarTodos()
    }

    void atualizarPerfilCompleto(Empresa e, Map dados) {
        e.nome = dados.nome ?: e.nome
        e.email = dados.email ?: e.email
        e.cnpj = dados.cnpj ?: e.cnpj
        e.descricao = dados.descricao ?: e.descricao
        e.pais = dados.pais ?: e.pais
        e.cep = dados.cep ?: e.cep

        empresaDAO.atualizar(e)
    }

    void excluirEmpresa(int id) {
        empresaDAO.deletar(id)
    }

    boolean vagaPertenceAEmpresa(int vagaId, int empresaId) {
        Vaga vaga = vagaDAO.listarTodos().find { it.id == vagaId }
        return vaga != null && vaga.empresa?.id == empresaId
    }

    Vaga buscarVagaPorId(int id) {
        return vagaDAO.listarTodos().find { it.id == id }
    }
}