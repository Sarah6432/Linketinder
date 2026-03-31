package service

import model.Empresa
import model.EmpresaDAO
import model.ICompetenciaManager
import model.ICurtida
import model.IReader
import model.IWriter
import model.Vaga
import model.VagaDAO

class EmpresaService {
    private final IReader<Empresa> empReader
    private final IWriter<Empresa> empWriter
    private final ICurtida empCurtida
    private final IReader<Vaga> vagaReader
    private final IWriter<Vaga> vagaWriter
    private final ICompetenciaManager vagaComp

    EmpresaService(EmpresaDAO eDAO, VagaDAO vDAO) {
        this.empReader = eDAO
        this.empWriter = eDAO
        this.empCurtida = eDAO
        this.vagaReader = vDAO
        this.vagaWriter = vDAO
        this.vagaComp = vDAO
    }

    void registrarEmpresa(Map d) {
        Empresa e = new Empresa(
                d.nome ?: "",
                d.email ?: "",
                d.pais ?: "",
                d.cep ?: "",
                d.desc ?: ""
        )
        e.cnpj = d.cnpj ?: ""
        e.senha = d.senha ?: ""

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
        Vaga v = vagaReader.listarTodos().find { it.id == vagaId }
        if (v) {
            if (dados.nome) v.nome = dados.nome
            if (dados.descricao) v.descricao = dados.descricao
            if (dados.local) v.localEstadoCidade = dados.local

            vagaWriter.atualizar(v)
            println "Vaga ID $vagaId atualizada com sucesso!"
        } else {
            println "Vaga não encontrada."
        }
    }
    boolean vagaPertenceAEmpresa(int vagaId, int empresaId) {
        def vaga = buscarVagaPorId(vagaId)
        return vaga != null && vaga.empresa?.id == empresaId
    }

    void excluirVaga(int id) {
        vagaWriter.deletar(id)
        println "Vaga Excluída com Sucesso!"
    }

    void gerenciarInteresse(int empId, int candId) {
        empCurtida.registrarCurtida(empId, candId)
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
        println "Dados Excluídos com Sucesso!"
    }

    void mostrarMatchesConfirmados(int empId) {
        ((EmpresaDAO)empReader).listarMatchesReais(empId).each {
            println "MATCH: Candidato ID ${it.candidatoId} - Vaga ID ${it.vagaId}"
        }
    }
}