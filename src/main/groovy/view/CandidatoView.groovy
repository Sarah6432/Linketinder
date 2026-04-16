package view

import model.Candidato
import controller.CandidatoController

class CandidatoView {
    private Scanner scanner = new Scanner(System.in)
    private CandidatoController controller

    CandidatoView(CandidatoController controller) {
        this.controller = controller
    }

    void gerenciarPerfil(Candidato candidatoLogado) {
        boolean emSessao = true

        while (emSessao) {
            try {
                int opcao = exibirMenuPerfil(candidatoLogado.nome)

                switch (opcao) {
                    case 1:
                        exibirDadosAtuais(candidatoLogado)
                        break

                    case 2:
                        Map novosDados = coletarDadosParaAtualizacao(candidatoLogado)
                        if (novosDados) {
                            controller.atualizarPerfil(candidatoLogado, novosDados)
                            mostrarMensagem("Perfil atualizado com sucesso!")
                        }
                        break

                    case 3:
                        if (confirmarExclusao()) {
                            controller.excluirCandidato(candidatoLogado.id)
                            mostrarMensagem("Conta removida.")
                            emSessao = false
                        }
                        break

                    case 4:
                        List vagas = controller.buscarVagasDisponiveis()
                        exibirVagasParaCandidato(vagas)
                        int idVaga = pedirIdVagaParaCurtir()
                        if (idVaga > 0) {
                            controller.curtirVaga(candidatoLogado.id, idVaga)
                            mostrarMensagem("Vaga curtida!")
                        }
                        break

                    case 5:
                        exibirMinhasCompetencias(candidatoLogado.competencias)
                        String novaSkill = pedirNomeNovaCompetencia()
                        if (!novaSkill.isEmpty()) {
                            controller.adicionarCompetencia(candidatoLogado.id, novaSkill)
                            candidatoLogado.competencias.add(novaSkill)
                            mostrarMensagem("Competência adicionada!")
                        }
                        break

                    case 6:
                        exibirMinhasCompetencias(candidatoLogado.competencias)
                        String skillRemover = pedirNomeCompetenciaParaRemover()
                        if (candidatoLogado.competencias.contains(skillRemover)) {
                            controller.removerCompetencia(candidatoLogado.id, skillRemover)
                            candidatoLogado.competencias.remove(skillRemover)
                            mostrarMensagem("Competência removida!")
                        } else {
                            mostrarMensagem("Competência não encontrada no seu perfil.")
                        }
                        break

                    case 0:
                        emSessao = false
                        break

                    default:
                        mostrarMensagem("Opção inválida.")
                        break
                }
            } catch (Exception e) {
                mostrarMensagem("Erro: ${e.message}")
            }
        }
    }

    int exibirMenuPerfil(String nomeCandidato) {
        println "\nBem vindo ao perfil do Candidato: $nomeCandidato"
        println "1. Visualizar Meus Dados"
        println "2. Editar Informações"
        println "3. Encerrar Minha Conta"
        println "4. Buscar Vagas e Curtir"
        println "5. Adicionar Competência"
        println "6. Remover Competência"
        println "0. Sair do Perfil"
        print "O que deseja fazer? "
        try {
            return Integer.parseInt(scanner.nextLine())
        } catch (Exception e) {
            return -1
        }
    }

    static void exibirDadosAtuais(Candidato candidato) {
        println "\nSeus dados atuais: "
        println "ID: ${candidato.id}"
        println "Nome Completo: ${candidato.nome} ${candidato.sobrenome}"
        println "E-mail: ${candidato.email}"
        println "País/CEP: ${candidato.pais} - ${candidato.cep}"
        println "Bio: ${candidato.descricao}"
        println "Competências: ${candidato.competencias?.join(', ') ?: 'Nenhuma registrada'}"
    }

    Map coletarDadosParaAtualizacao(Candidato candidato) {
        println "\nEditar Perfil (Enter para manter atual):"
        print "Novo nome (${candidato.nome}): "
        String nomeInput = scanner.nextLine()
        print "Novo sobrenome (${candidato.sobrenome}): "
        String sobrenomeInput = scanner.nextLine()
        print "Novo Email (${candidato.email}): "
        String emailInput = scanner.nextLine()
        print "Nova Bio (${candidato.descricao}): "
        String descricaoInput = scanner.nextLine()
        print "Novas competências (separadas por vírgula): "
        String skillsRaw = scanner.nextLine()

        return [
                nome: nomeInput.isEmpty() ? candidato.nome : nomeInput,
                sobrenome: sobrenomeInput.isEmpty() ? candidato.sobrenome : sobrenomeInput,
                email: emailInput.isEmpty() ? candidato.email : emailInput,
                descricao: descricaoInput.isEmpty() ? candidato.descricao : descricaoInput,
                competencias: skillsRaw.isEmpty() ? candidato.competencias?.join(',') : skillsRaw
        ]
    }

    boolean confirmarExclusao() {
        println "Alerta! - Seu Perfil será excluído permanentemente"
        print "Tem certeza que deseja continuar? (S/N): "
        return scanner.nextLine().trim().equalsIgnoreCase("S")
    }

    static void exibirVagasParaCandidato(List vagas) {
        println "\nVagas Disponíveis: "
        vagas.each { v ->
            println "ID: ${v.id} | [${v.nome}] em ${v.localEstadoCidade}"
            println "Descrição: ${v.descricao}"
            println "Requisitos: ${v.competencias?.join(', ')}"
        }
    }

    int pedirIdVagaParaCurtir() {
        print "\nDigite o ID da vaga que deseja curtir (0 para voltar): "
        try {
            return Integer.parseInt(scanner.nextLine())
        } catch (Exception e) {
            return -1
        }
    }

    String pedirNomeNovaCompetencia() {
        print "Digite o nome da nova competência: "
        return scanner.nextLine().trim()
    }

    static void exibirMinhasCompetencias(List skills) {
        println "\nSuas competências: ${skills.join(', ')}"
    }

    String pedirNomeCompetenciaParaRemover() {
        print "Digite o nome da competência para remover: "
        return scanner.nextLine().trim()
    }

    static void mostrarMensagem(String msg) {
        println "- [SISTEMA]: $msg"
    }
}