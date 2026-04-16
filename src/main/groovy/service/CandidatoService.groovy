package service

import model.Candidato
import model.Vaga
import DAO.CandidatoDAO
import DAO.VagaDAO

class CandidatoService {
    private final CandidatoDAO candidatoDAO
    private final VagaDAO vagaDAO

    CandidatoService(CandidatoDAO candidatoDAO, VagaDAO vagaDAO) {
        this.candidatoDAO = candidatoDAO
        this.vagaDAO = vagaDAO
    }

    int registrarNovoCandidato(Map d) {
        List<String> skills = (d.skills instanceof List) ? (List<String>) d.skills : []

        Candidato c = new Candidato(
                (String) d.nome ?: "",
                (String) d.email ?: "",
                (String) d.pais ?: "",
                (String) d.cep ?: "",
                (String) d.bio ?: "",
                (String) d.cpf ?: "",
                skills
        )

        c.sobrenome = (String) d.sobrenome ?: ""
        c.dataNascimento = (String) d.data ?: ""
        c.senha = (String) d.senha ?: ""

        int id = candidatoDAO.salvar(c)

        if (id > 0 && skills) {
            skills.each { String skill ->
                candidatoDAO.vincularCompetencia(id, skill)
            }
        }
        return id
    }

    void atualizarPerfilCompleto(Candidato c, Map dados) {
        c.nome = (String) dados.nome ?: c.nome
        c.sobrenome = (String) dados.sobrenome ?: c.sobrenome
        c.email = (String) dados.email ?: c.email
        c.descricao = (String) dados.descricao ?: c.descricao

        candidatoDAO.atualizar(c)

        if (dados.competencias instanceof String) {
            List<String> novasSkills = dados.competencias.split(',')
                    .collect { it.trim() }
                    .findAll { !it.isEmpty() }
                    .unique()

            candidatoDAO.atualizarCompetenciasNoBanco(c.id, novasSkills)
            c.competencias = novasSkills
        }
    }

    List<Vaga> listarVagasDisponiveis() {
        return (List<Vaga>) vagaDAO.listarTodos()
    }

    void curtirVaga(int candidatoId, int vagaId) {
        candidatoDAO.registrarCurtida(candidatoId, vagaId)
    }

    void excluirCandidato(int id) {
        candidatoDAO.deletar(id)
    }

    void adicionarCompetencia(int candidatoId, String skill) {
        if (skill && !skill.trim().isEmpty()) {
            candidatoDAO.vincularCompetencia(candidatoId, skill.trim())
        }
    }

    void removerCompetencia(int candidatoId, String skill) {
        candidatoDAO.desvincularCompetencia(candidatoId, skill)
    }
}