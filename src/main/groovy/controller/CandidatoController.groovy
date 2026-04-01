package controller

import model.Candidato
import service.CandidatoService
import view.CandidatoView

class CandidatoController {
    private final CandidatoService service
    private final CandidatoView view

    CandidatoController(CandidatoService service, CandidatoView view) {
        this.service = service
        this.view = view
    }

    void gerenciarPerfil(Candidato candidatoLogado) {
        boolean emSessao = true

        while (emSessao) {
            try {
                int opcao = view.exibirMenuPerfil(candidatoLogado.nome)

                switch (opcao) {
                    case 1:
                        view.exibirDadosAtuais(candidatoLogado)
                        break

                    case 2:
                        Map novosDados = view.coletarDadosParaAtualizacao(candidatoLogado)
                        if (novosDados) {
                            service.atualizarPerfilCompleto(candidatoLogado, novosDados)
                            view.mostrarMensagem("Perfil atualizado com sucesso!")
                        }
                        break

                    case 3:
                        if (view.confirmarExclusao()) {
                            service.excluirCandidato(candidatoLogado.id)
                            view.mostrarMensagem("Conta removida.")
                            emSessao = false
                        }
                        break

                    case 4:
                        List vagas = service.listarVagasDisponiveis()
                        view.exibirVagasParaCandidato(vagas)
                        int idVaga = view.pedirIdVagaParaCurtir()
                        if (idVaga > 0) {
                            service.curtirVaga(candidatoLogado.id, idVaga)
                            view.mostrarMensagem("Vaga curtida!")
                        }
                        break

                    case 5:
                        view.exibirMinhasCompetencias(candidatoLogado.competencias)
                        String novaSkill = view.pedirNomeNovaCompetencia()
                        if (!novaSkill.isEmpty()) {
                            service.adicionarCompetencia(candidatoLogado.id, novaSkill)
                            candidatoLogado.competencias.add(novaSkill)
                            view.mostrarMensagem("Competência adicionada!")
                        }
                        break

                    case 6:
                        view.exibirMinhasCompetencias(candidatoLogado.competencias)
                        String skillRemover = view.pedirNomeCompetenciaParaRemover()
                        if (candidatoLogado.competencias.contains(skillRemover)) {
                            service.removerCompetencia(candidatoLogado.id, skillRemover)
                            candidatoLogado.competencias.remove(skillRemover)
                            view.mostrarMensagem("Competência removida!")
                        } else {
                            view.mostrarMensagem("Competência não encontrada no seu perfil.")
                        }
                        break

                    case 0:
                        emSessao = false
                        break

                    default:
                        view.mostrarMensagem("Opção inválida.")
                        break
                }
            } catch (Exception e) {
                view.mostrarMensagem("Erro: ${e.message}")
            }
        }
    }
}