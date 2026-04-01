package service

import model.Candidato
import model.CandidatoDAO
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

    void atualizarPerfilCompleto(Candidato c, Map dados) {
        c.nome = dados.nome
        c.sobrenome = dados.sobrenome
        c.email = dados.email
        c.descricao = dados.descricao

        writer.atualizar(c)

        if (dados.competencias != null) {
            List<String> novasSkills = dados.competencias.split(',')
                    .collect { it.trim() }
                    .findAll { !it.isEmpty() }
                    .unique()

            ((CandidatoDAO) writer).atualizarCompetencias(c.id, novasSkills)
            c.competencias = novasSkills
        }
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