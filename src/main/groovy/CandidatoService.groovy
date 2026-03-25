class CandidatoService {
    private final IReader<Candidato> reader
    private final IWriter<Candidato> writer
    private final ICompetenciaManager compManager

    CandidatoService(IReader<Candidato> reader, IWriter<Candidato> writer, ICompetenciaManager compManager) {
        this.reader = reader
        this.writer = writer
        this.compManager = compManager
    }

    void registrarNovoCandidato(String nome, String sobrenome, String data, String email, String cpf, String pais, String cep, String descricao, String senha, List<String> skills) {
        Candidato c = new Candidato(nome, email, pais, cep, descricao, cpf, skills)
        c.sobrenome = sobrenome
        c.dataNascimento = data
        c.senha = senha
        int id = writer.salvar(c)
        if (id != -1) skills.each { compManager.vincular(id, it) }
        println "Candidato ID $id cadastrado!"
    }

    void atualizarPerfilCompleto(Candidato c, Map dados) {
        if (dados.nome) c.nome = dados.nome
        if (dados.sobrenome) c.sobrenome = dados.sobrenome
        if (dados.email) c.email = dados.email
        writer.atualizar(c)
        println "Dados Atualizados com Sucesso!"
    }

    void listarCandidatos() {
        reader.listarTodos().each { it.exibirPerfil() }
    }

    void excluirCandidato(int id) {
        writer.deletar(id)
        println "Dados Excluidos com Sucesso!"
    }

    List<Candidato> obterTodos() {
        return reader.listarTodos()
    }
}