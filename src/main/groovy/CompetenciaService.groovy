class CompetenciaService {
    private final CompetenciaDAO competenciaDAO
    private final ICompetenciaManager compManager

    CompetenciaService(CompetenciaDAO compDAO, ICompetenciaManager manager) {
        this.competenciaDAO = compDAO
        this.compManager = manager
    }

    void vincularAoCandidato(int candidatoId, String nomeSkill) {
        try {
            compManager.vincular(candidatoId, nomeSkill)
            println "Sucesso: Skill '$nomeSkill' vinculada ao candidato $candidatoId!"
        } catch (Exception e) {
            println "Erro ao vincular: ${e.message}"
        }
    }

    void listarTodas() {
        competenciaDAO.listar().each { println "ID: ${it.id} | Nome: ${it.nome}" }
    }

    void salvarCompetencia(String nome) {
        competenciaDAO.salvar(nome)
        println "Sucesso: Competência '$nome' criada!"
    }

    void atualizarCompetencia(int id, String novoNome) {
        competenciaDAO.atualizar(id, novoNome)
        println "Sucesso: Competência ID $id atualizada para '$novoNome'!"
    }

    void excluirCompetencia(int id) {
        competenciaDAO.deletar(id)
        println "Sucesso: Competência removida!"
    }
}