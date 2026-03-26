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

    void registrarEmpresa(String nome, String email, String cnpj, String cep, String pais, String desc, String senha) {
        Empresa e = new Empresa(nome, email, cep, pais, desc, cnpj)
        e.senha = senha
        empWriter.salvar(e)
        println "Empresa cadastrada!"
    }

    void anunciarVaga(int empresaId, String titulo, String desc, String local, List<String> reqs) {
        Empresa emp = new Empresa("", "", "", "", "", "")
        emp.id = empresaId
        Vaga v = new Vaga(titulo, desc, reqs, emp)
        v.localEstadoCidade = local
        int idVaga = ((VagaDAO)vagaWriter).salvar(v, empresaId)
        reqs.each { vagaComp.vincular(idVaga, it) }
        println "Vaga anunciada!"
    }

    void listarVagasPorEmpresa(int empresaId) {
        vagaReader.listarTodos().findAll { it.empresa.id == empresaId }.each { it.exibirVaga(0) }
    }

    void atualizarVagaCompleta(int vagaId, Map dados) {
        Vaga v = vagaReader.listarTodos().find { it.id == vagaId }
        if (v) {
            if (dados.nome) v.nome = dados.nome
            if (dados.descricao) v.descricao = dados.descricao

            vagaWriter.atualizar(v)
            println "Vaga ID $vagaId atualizada com sucesso!"
        } else {
            println "Vaga não encontrada."
        }
    }

    void excluirVaga(int id) {
        vagaWriter.deletar(id)
        println "Vaga Excluida com Sucesso!"
    }

    void gerenciarInteresse(int empId, int candId, ICurtida cDAO) {
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
        println "Dados Excluidos com Sucesso!"
    }

    void mostrarMatchesConfirmados(int empId) {
        ((EmpresaDAO)empReader).listarMatchesReais(empId).each { println "MATCH: ${it.candidato} - ${it.vaga}" }
    }
}