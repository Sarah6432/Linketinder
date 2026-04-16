package controller

import model.Candidato
import service.CandidatoService

class CandidatoController {
    private final CandidatoService service

    CandidatoController(CandidatoService service) {
        this.service = service
    }

    void atualizarPerfil(Candidato candidato, Map novosDados) {
        service.atualizarPerfilCompleto(candidato, novosDados)
    }

    void excluirCandidato(int id) {
        service.excluirCandidato(id)
    }

    List buscarVagasDisponiveis() {
        return service.listarVagasDisponiveis()
    }

    void curtirVaga(int candidatoId, int vagaId) {
        service.curtirVaga(candidatoId, vagaId)
    }

    void adicionarCompetencia(int id, String skill) {
        service.adicionarCompetencia(id, skill)
    }

    void removerCompetencia(int id, String skill) {
        service.removerCompetencia(id, skill)
    }
}