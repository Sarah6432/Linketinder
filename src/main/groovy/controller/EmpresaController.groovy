package controller

import model.Empresa
import service.EmpresaService
import view.EmpresaView

class EmpresaController {
    private final EmpresaService service
    private final EmpresaView view

    EmpresaController(EmpresaService service, EmpresaView view) {
        this.service = service
        this.view = view
    }

    void gerenciarPainel(Empresa empresaLogada) {
        boolean emSessao = true

        while (emSessao) {
            int opcao = view.exibirMenuEmpresa(empresaLogada.nome)

            switch (opcao) {
                case 1:
                    view.exibirDadosEmpresa(empresaLogada)
                    break

                case 2:
                    Map novosDados = view.coletarEdicaoPerfil(empresaLogada)
                    service.atualizarPerfilCompleto(empresaLogada, novosDados)
                    view.mostrarMensagem("Perfil da empresa atualizado com sucesso!")
                    break

                case 3:
                    Map dadosVaga = view.coletarDadosNovaVaga()
                    if (dadosVaga.valido) {
                        List<String> requisitos = dadosVaga.requisitosRaw.split(',').collect { it.trim() }
                        service.anunciarVaga(empresaLogada.id, dadosVaga.titulo, dadosVaga.descricao, dadosVaga.local, requisitos)
                        view.mostrarMensagem("Vaga anunciada com sucesso!")
                    }
                    break

                case 4:
                    def vagas = service.listarVagasPorEmpresa(empresaLogada.id) ?: []
                    if (vagas == null || vagas.isEmpty()) {
                        view.mostrarMensagem("Sua empresa ainda não possui vagas cadastradas.")
                    } else {
                        vagas.each { v -> println "ID: ${v.id} | Título: ${v.nome} | Local: ${v.localEstadoCidade}" }
                    }
                    break

                case 5:
                    int idVaga = view.pedirId("editar")
                    def vaga = service.buscarVagaPorId(idVaga)
                    if (vaga && vaga.empresa?.id == empresaLogada.id) {
                        Map edit = view.coletarEdicaoVaga(vaga)
                        service.atualizarVagaCompleta(idVaga, edit)
                        view.mostrarMensagem("Vaga atualizada!")
                    } else {
                        view.mostrarMensagem("Vaga não encontrada ou acesso negado.")
                    }
                    break

                case 6:
                    int idExcluir = view.pedirId("excluir")
                    if (service.vagaPertenceAEmpresa(idExcluir, empresaLogada.id)) {
                        service.excluirVaga(idExcluir)
                        view.mostrarMensagem("Vaga excluída com sucesso!")
                    } else {
                        view.mostrarMensagem("Erro: Vaga não encontrada ou você não tem permissão.")
                    }
                    break

                case 7:
                    println "\n--- Gestão de Matches ---"
                    println "1. Ver Candidatos que curtiram minhas vagas e Retribuir (Like)"
                    println "2. Visualizar Matches Confirmados (Interesse Mútuo)"

                    String sub = view.lerEntrada("Escolha: ").trim()

                    if (sub == "1") {
                        def interessados = service.listarCandidatosQueCurtiuEmpresa(empresaLogada.id)
                        if (interessados.isEmpty()) {
                            println "Ninguém curtiu suas vagas ainda."
                        } else {
                            interessados.each { c ->
                                println "------------------------------------------"
                                println "ID: ${c.id} | Candidato: ${c.nome}"
                                println "Vaga de interesse: ${c.nome_vaga}"
                                println "Competências: ${c.competencias ?: 'Não informadas'}"
                            }
                            int idCand = view.pedirId("demonstrar interesse")
                            service.gerenciarInteresse(empresaLogada.id, idCand)
                        }
                    } else if (sub == "2") {
                        service.listarMatchesDaEmpresa(empresaLogada.id)
                    }
                    else {
                        println "Opção inválida."
                    }
                    break

                case 8:
                    if (view.confirmarExclusaoEmpresa()) {
                        service.excluirEmpresa(empresaLogada.id)
                        view.mostrarMensagem("Empresa e vagas removidas do sistema.")
                        emSessao = false
                    }
                    break

                case 0:
                    emSessao = false
                    break

                default:
                    view.mostrarMensagem("Opção inválida.")
                    break
            }
        }
    }
}