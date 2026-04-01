package service

import model.CandidatoDAO
import model.Empresa
import model.EmpresaDAO
import model.ICompetenciaManager
import model.ICurtida
import model.IReader
import model.IWriter
import model.Vaga
import model.VagaDAO
import view.EmpresaView

class EmpresaService {
    private final IReader<Empresa> empReader
    private final IWriter<Empresa> empWriter
    private final ICurtida empCurtida
    private final IReader<Vaga> vagaReader
    private final IWriter<Vaga> vagaWriter
    private final ICompetenciaManager vagaComp
    private final CandidatoDAO candReader
    private final EmpresaView view

    EmpresaService(EmpresaDAO eDAO, VagaDAO vDAO, CandidatoDAO cDAO, EmpresaView view) {
        this.empReader = eDAO
        this.empWriter = eDAO
        this.empCurtida = eDAO
        this.vagaReader = vDAO
        this.vagaWriter = vDAO
        this.vagaComp = vDAO
        this.candReader = cDAO
        this.view = view
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

        empWriter.salvar(e)
    }


    void anunciarVaga(int empresaId, String titulo, String desc, String local, List<String> reqs) {
        Empresa emp = new Empresa()
        emp.id = empresaId

        Vaga v = new Vaga(titulo, desc, reqs, emp)
        v.localEstadoCidade = local

        int idVaga = vagaWriter.salvar(v)

        if (idVaga > 0) {
            reqs.each { vagaComp.vincular(idVaga, it) }
            println "Vaga anunciada com sucesso!"
        }
    }

    List<Vaga> listarVagasPorEmpresa(int empresaId) {
        def todasAsVagas = vagaReader.listarTodos() ?: []
        return todasAsVagas.findAll { it != null && it.empresa != null && it.empresa.id == empresaId }
    }

    Vaga buscarVagaPorId(int id) {
        return vagaReader.listarTodos().find { it.id == id }
    }

    void atualizarVagaCompleta(int vagaId, Map dados) {
        def dao = (model.VagaDAO) vagaWriter
        dao.atualizarBasico(vagaId, dados.nome, dados.descricao)

        if (dados.requisitos != null) {
            List<String> novosRequisitos = dados.requisitos.split(',')
                    .collect { it.trim() }
                    .findAll { !it.isEmpty() }
                    .unique()

            dao.limparCompetenciasVaga(vagaId)
            novosRequisitos.each { req ->
                dao.vincularCompetenciaVaga(vagaId, req)
            }
        }
    }

    boolean vagaPertenceAEmpresa(int vagaId, int empresaId) {
        def vaga = buscarVagaPorId(vagaId)
        return vaga != null && vaga.empresa?.id == empresaId
    }

    void excluirVaga(int id) {
        vagaWriter.deletar(id)
        println "Vaga Excluida com Sucesso!"
    }

    void gerenciarInteresse(int empId, int candId) {
        empCurtida.registrarCurtida(empId, candId)

        def daoEmpresa = (model.EmpresaDAO) empReader
        def matches = daoEmpresa.buscarMatchesPorEmpresa(empId) ?: []

        boolean deuMatch = matches.any { it.candidatoId == candId }

        if (deuMatch) {
            println "\nmatch confirmado!"
            def perfil = candReader.buscarPerfilCompleto(candId)
            view.exibirPerfilDetalhado(perfil)
        } else {
            println "\ninteresse registrado. aguardando retribuicao do candidato."
        }
    }

    List listarCandidatosQueCurtiuEmpresa(int empId) {
        return ((EmpresaDAO) empReader).listarCandidatosInteressados(empId) ?: []
    }

    void listarMatchesDaEmpresa(int empId) {
        def daoEmpresa = (model.EmpresaDAO) empReader
        def matches = daoEmpresa.buscarMatchesPorEmpresa(empId) ?: []

        if (matches.isEmpty()) {
            println "\nNenhum match encontrado ate o momento."
        } else {
            println "\nSeus Matches: "
            matches.each { m ->
                println "------------------------------------------"
                println "Candidato: ${m.nomeCandidato}"
                println "E-mail: ${m.emailCandidato}"
                println "Vaga Relacionada: ${m.nomeVaga}"
                println "------------------------------------------"
            }
        }
    }

    List<Empresa> obterTodas() {
        return empReader.listarTodos()
    }

    void atualizarPerfilCompleto(Empresa e, Map dados) {
        if (dados.nome) e.nome = dados.nome
        if (dados.email) e.email = dados.email
        if (dados.cnpj) e.cnpj = dados.cnpj
        if (dados.descricao) e.descricao = dados.descricao
        if (dados.pais) e.pais = dados.pais
        if (dados.cep) e.cep = dados.cep

        empWriter.atualizar(e)
        println "Perfil da empresa '${e.nome}' atualizado com sucesso!"
    }

    void excluirEmpresa(int id) {
        empWriter.deletar(id)
        println "Dados Excluidos com Sucesso!"
    }
}