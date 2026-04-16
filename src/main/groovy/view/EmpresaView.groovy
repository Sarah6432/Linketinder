package view

import model.Candidato
import model.Empresa
import model.Vaga
import controller.EmpresaController

class EmpresaView {
    private Scanner scanner = new Scanner(System.in)
    private EmpresaController controller

    EmpresaView(EmpresaController controller) {
        this.controller = controller
    }

    void gerenciarPainel(Empresa empresaLogada) {
        boolean emSessao = true

        while (emSessao) {
            try {
                int opcao = exibirMenuEmpresa(empresaLogada.nome)

                switch (opcao) {
                    case 1:
                        exibirDadosAtuais(empresaLogada)
                        break

                    case 2:
                        Map novosDados = coletarEdicaoPerfil(empresaLogada)
                        controller.atualizarPerfil(empresaLogada, novosDados)
                        mostrarMensagem("Perfil da empresa atualizado com sucesso!")
                        break

                    case 3:
                        Map dadosVaga = coletarDadosNovaVaga()
                        if (dadosVaga.valido) {
                            List<String> requisitos = (List<String>) dadosVaga.requisitosRaw.split(',').collect { it.trim() }
                            controller.anunciarVaga(empresaLogada.id, (String) dadosVaga.titulo, (String) dadosVaga.descricao, (String) dadosVaga.local, requisitos)
                            mostrarMensagem("Vaga anunciada com sucesso!")
                        }
                        break

                    case 4:
                        List<Vaga> vagas = controller.listarVagasPorEmpresa(empresaLogada.id)
                        if (vagas.isEmpty()) {
                            mostrarMensagem("Sua empresa ainda não possui vagas cadastradas.")
                        } else {
                            vagas.each { Vaga v -> println "ID: ${v.id} | Título: ${v.nome} | Local: ${v.localEstadoCidade}" }
                        }
                        break

                    case 5:
                        int idVaga = pedirId("editar")
                        Vaga vaga = (Vaga) controller.buscarVagaPorId(idVaga)
                        if (vaga) {
                            Map dadosEditados = coletarEdicaoVaga(vaga)
                            controller.atualizarVaga(idVaga, dadosEditados)
                            mostrarMensagem("Vaga atualizada com sucesso!")
                        } else {
                            mostrarMensagem("Vaga não encontrada.")
                        }
                        break

                    case 6:
                        int idExcluir = pedirId("excluir")
                        if (controller.verificarPropriedadeVaga(idExcluir, empresaLogada.id)) {
                            controller.excluirVaga(idExcluir)
                            mostrarMensagem("Vaga excluída com sucesso!")
                        } else {
                            mostrarMensagem("Erro: Vaga não encontrada ou sem permissão.")
                        }
                        break

                    case 7:
                        fluxoMatches(empresaLogada.id)
                        break

                    case 8:
                        if (confirmarExclusaoEmpresa()) {
                            controller.excluirContaEmpresa(empresaLogada.id)
                            mostrarMensagem("Empresa removida do sistema.")
                            emSessao = false
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
               e.printStackTrace()
            }
        }
    }

    private void fluxoMatches(int empresaId) {
        println "\nGestao de Matches:"
        println "1. Ver Candidatos que curtiram minhas vagas"
        println "2. Visualizar Matches Confirmados"
        String sub = lerEntrada("Escolha: ").trim()

        if (sub == "1") {
            List<Map> interessados = controller.listarInteressados(empresaId)

            if (interessados.isEmpty()) {
                println "Ninguém curtiu suas vagas ainda."
            } else {
                println "\nCandidatos Interessados: "
                interessados.each { Map c ->
                    println "ID: ${c.id} | Vaga: ${c.nome_vaga}"
                    println "Skills do candidato: ${c.competencias ?: 'Nao informadas'}"
                }

                int idCand = pedirId("dar like (0 para cancelar)")

                if (idCand > 0) {
                    Map dadosMatch = controller.gerenciarInteresse(empresaId, idCand)

                    if (dadosMatch) {
                        println "\nMATCH CONFIRMADO!"
                        println "Nome: ${dadosMatch.nome} ${dadosMatch.sobrenome ?: ''}"
                        println "E-mail: ${dadosMatch.email}"
                        println "CPF: ${dadosMatch.cpf}"
                        println "Bio: ${dadosMatch.bio ?: 'Nao informada'}"
                    } else {
                        println "\nInteresse registrado. Aguardando retribuicao do candidato."
                    }
                }
            }
        } else if (sub == "2") {
            exibirMatches(empresaId)
        }
    }

    void exibirMatches(int empresaId) {
        List<Candidato> matches = controller.obterMatchesCompletos(empresaId) ?: []

        if (matches.isEmpty()) {
            println "\nNenhum match confirmado ate o momento."
        } else {
            println "\nMATCHES CONFIRMADOS:"
            matches.each { Candidato c ->
                println "--------------------------"
                c.exibirPerfil()
                println "E-mail de contato: ${c.email}"
                println "--------------------------"
            }
        }
    }

    int exibirMenuEmpresa(String nomeEmpresa) {
        println "\nPainel da Empresa - $nomeEmpresa"
        println "1. Ver Perfil | 2. Editar Perfil | 3. Anunciar Vaga"
        println "4. Listar Vagas | 5. Editar Vaga | 6. Excluir Vaga"
        println "7. Matches | 8. Encerrar Conta | 0. Sair"
        print "Escolha: "
        try {
            return Integer.parseInt(scanner.nextLine())
        } catch (Exception e) {
            e.printStackTrace()
            return -1
        }
    }

    Map coletarDadosNovaVaga() {
        println "Anunciar nova vaga: "
        print "Titulo: "; String t = scanner.nextLine()
        print "Descrição: "; String d = scanner.nextLine()
        print "Local: "; String l = scanner.nextLine()
        print "Requisitos (vítgula): "; String r = scanner.nextLine()
        return [titulo: t, descricao: d, local: l, requisitosRaw: r, valido: true]
    }

    Map coletarEdicaoVaga(Vaga vaga) {
        print "Novo Título [${vaga.nome}]: "; String nome = scanner.nextLine()
        print "Nova Descrição [${vaga.descricao}]: "; String desc = scanner.nextLine()
        print "Novos Requisitos: "; String req = scanner.nextLine()
        return [
                nome: nome.isEmpty() ? vaga.nome : nome,
                descricao: desc.isEmpty() ? vaga.descricao : desc,
                requisitos: req.isEmpty() ? (String) vaga.competencias?.join(',') : req
        ]
    }

    static void exibirDadosAtuais(Empresa emp) {
        println "Empresa: ${emp.nome} | CNPJ: ${emp.cnpj} | Email: ${emp.email}"
    }

    Map coletarEdicaoPerfil(Empresa emp) {
        print "Novo Nome (${emp.nome}): "; String n = scanner.nextLine()
        print "Novo Email (${emp.email}): "; String e = scanner.nextLine()
        return [
                nome: n.isEmpty() ? emp.nome : n,
                email: e.isEmpty() ? emp.email : e,
                descricao: emp.descricao
        ]
    }

    boolean confirmarExclusaoEmpresa() {
        print "Tem certeza que deseja excluir a empresa? (S/N): "
        return scanner.nextLine().trim().equalsIgnoreCase("S")
    }

    int pedirId(String acao) {
        print "Digite o ID para $acao: "
        try {
            return Integer.parseInt(scanner.nextLine())
        } catch (Exception e) {
            e.printStackTrace()
            return -1
        }
    }

    static void mostrarMensagem(String msg) {
        println "- [SISTEMA]: $msg"
    }

    String lerEntrada(String mensagem) {
        print mensagem
        return scanner.nextLine()
    }
}