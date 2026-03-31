package service

import model.Candidato
import model.ICompetenciaManager
import model.IReader
import model.IWriter
import model.Vaga

class CandidatoService {
    private final IReader<Candidato> reader
    private final IWriter<Candidato> writer
    private final ICompetenciaManager compManager
    private final IReader<Vaga> vagaReader

    CandidatoService(IReader<Candidato> reader, IWriter<Candidato> writer, ICompetenciaManager compManager, IReader<Vaga> vagaReader) {
        this.reader = reader
        this.writer = writer
        this.compManager = compManager
        this.vagaReader = vagaReader
    }

    int registrarNovoCandidato(Map d) {
        Candidato c = new Candidato(
                d.nome ?: "",
                d.email ?: "",
                d.pais ?: "",
                d.cep ?: "",
                d.bio ?: "",
                d.cpf ?: "",
                d.skills ?: []
        )
        c.sobrenome = d.sobrenome ?: ""
        c.dataNascimento = d.data ?: ""
        c.senha = d.senha ?: ""

        int id = writer.salvar(c)

        if (id > 0 && d.skills) {
            d.skills.each { skill ->
                compManager.vincular(id, skill.toString())
            }
        }
        return id
    }

    Candidato atualizarPerfilCompleto(Candidato c, Map dados) {
        if (dados.nome) c.nome = dados.nome
        if (dados.sobrenome) c.sobrenome = dados.sobrenome
        if (dados.email) c.email = dados.email
        if (dados.descricao) c.descricao = dados.descricao

        writer.atualizar(c)
        return c
    }

    List<Vaga> listarVagasDisponiveis() {
        return vagaReader.listarTodos()
    }

    void curtirVaga(int candidatoId, int vagaId) {
        if (writer instanceof model.ICurtida) {
            ((model.ICurtida) writer).registrarCurtida(candidatoId, vagaId)
        }
    }

    boolean excluirCandidato(int id) {
        writer.deletar(id)
        return true
    }

    void adicionarCompetencia(int candidatoId, String skill) {
        compManager.vincular(candidatoId, skill)
    }

    void removerCompetencia(int candidatoId, String skill) {
        compManager.desvincular(candidatoId, skill)
    }
}