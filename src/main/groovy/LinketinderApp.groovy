import java.util.Scanner

class LinketinderApp {
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in)
        def sql = Conexao.getConn()

        CandidatoDAO cDAO = new CandidatoDAO(sql)
        EmpresaDAO eDAO = new EmpresaDAO(sql)
        VagaDAO vDAO = new VagaDAO(sql)
        CompetenciaDAO compDAO = new CompetenciaDAO(sql)

        CandidatoService candidatoService = new CandidatoService(cDAO, cDAO, cDAO)
        EmpresaService empresaService = new EmpresaService(eDAO, vDAO)
        CompetenciaService competenciaService = new CompetenciaService(compDAO, cDAO)

        while (true) {
            exibirMenuPrincipal()
            String opcaoInput = scanner.nextLine()
            if (opcaoInput == "0") break

            try {
                int opcao = Integer.parseInt(opcaoInput)
                switch (opcao) {
                    case 1: menuGerenciamentoGeral(scanner, candidatoService, empresaService); break
                    case 2: fluxoAreaEmpresa(scanner, empresaService, eDAO, cDAO); break
                    case 3: fluxoAreaCandidato(scanner, cDAO, vDAO); break
                    case 4: fluxoGerenciarMatches(scanner, eDAO, cDAO, empresaService); break
                    case 5: fluxoCadastroCandidato(scanner, candidatoService); break
                    case 6: fluxoCadastroEmpresa(scanner, empresaService); break
                    case 9: menuGerenciarCompetencias(scanner, competenciaService); break
                    default: println "Opção inválida."
                }
            } catch (Exception e) {
                println "Erro no sistema: ${e.message}"
            }
        }
    }

    private static void exibirMenuPrincipal() {
        println "\nLINKETINDER"
        println "1. Gerenciamento Geral (Admin)"
        println "2. Área da Empresa"
        println "3. Área do Candidato"
        println "4. Matches"
        println "5. Novo Candidato"
        println "6. Nova Empresa"
        println "9. Competências"
        println "0. Sair"
        print "Escolha: "
    }

    private static void menuGerenciamentoGeral(Scanner scanner, CandidatoService cService, EmpresaService eService) {
        println "\n1. Listar Tudo | 2. Atualizar Candidato | 3. Excluir Candidato | 4. Atualizar Empresa | 5. Excluir Empresa"
        int op = Integer.parseInt(scanner.nextLine())

        if (op == 1) {
            cService.listarCandidatos()
            eService.obterTodas().each { it.exibirPerfil() }
        } else if (op == 2) {
            def lista = cService.obterTodos()
            lista.each { println "ID: ${it.id} - ${it.nome}" }
            print "ID para atualizar: "; int id = Integer.parseInt(scanner.nextLine())
            Candidato c = lista.find { it.id == id }
            if (c) {
                def dados = [:]
                print "Novo Nome (${c.nome}): "; String n = scanner.nextLine(); if (!n.isEmpty()) dados.nome = n
                print "Novo Email (${c.email}): "; String e = scanner.nextLine(); if (!e.isEmpty()) dados.email = e
                print "Nova Bio (${c.descricao}): "; String d = scanner.nextLine(); if (!d.isEmpty()) dados.descricao = d
                cService.atualizarPerfilCompleto(c, dados)
            }
        } else if (op == 3) {
            print "ID para excluir: "; int id = Integer.parseInt(scanner.nextLine())
            cService.excluirCandidato(id)
        } else if (op == 4) {
            def lista = eService.obterTodas()
            lista.each { println "ID: ${it.id} - ${it.nome}" }
            print "ID para atualizar: "; int id = Integer.parseInt(scanner.nextLine())
            Empresa emp = lista.find { it.id == id }
            if (emp) {
                def dados = [:]
                print "Novo Nome (${emp.nome}): "; String n = scanner.nextLine(); if (!n.isEmpty()) dados.nome = n
                print "Novo Email (${emp.email}): "; String e = scanner.nextLine(); if (!e.isEmpty()) dados.email = e
                eService.atualizarPerfilCompleto(emp, dados)
            }
        } else if (op == 5) {
            print "ID para excluir: "; int id = Integer.parseInt(scanner.nextLine())
            eService.excluirEmpresa(id)
        }
    }

    private static void fluxoAreaEmpresa(Scanner scanner, EmpresaService service, EmpresaDAO eDAO, CandidatoDAO cDAO) {
        println "\n1. Criar Vaga | 2. Listar Minhas Vagas | 3. Atualizar Vaga | 4. Deletar Vaga"
        int op = Integer.parseInt(scanner.nextLine())
        print "ID da sua Empresa: "; int idEmp = Integer.parseInt(scanner.nextLine())

        switch (op) {
            case 1:
                print "Título: "; String t = scanner.nextLine()
                print "Descrição: "; String d = scanner.nextLine()
                print "Local: "; String l = scanner.nextLine()
                print "Requisitos: "; String r = scanner.nextLine()
                service.anunciarVaga(idEmp, t, d, l, r.split(',').toList())
                break
            case 2:
                service.listarVagasPorEmpresa(idEmp); break
            case 3:
                print "ID Vaga: "; int idV = Integer.parseInt(scanner.nextLine())
                def dados = [:]
                print "Novo Título: "; String nt = scanner.nextLine(); if(!nt.isEmpty()) dados.nome = nt
                service.atualizarVagaCompleta(idV, dados); break
            case 4:
                print "ID Vaga para deletar: "; int idDel = Integer.parseInt(scanner.nextLine())
                service.excluirVaga(idDel); break
        }
    }
    private static void fluxoAreaCandidato(Scanner scanner, CandidatoDAO cDAO, VagaDAO vDAO) {
        try {
            def vagas = vDAO.listarTodos()
            if (vagas.isEmpty()) return

            vagas.each { v ->
                println "ID: ${v.id} | Vaga: ${v.nome}"
                println "Requisitos: ${v.competencias?.join(', ') ?: 'Nenhum'}"
            }

            print "Seu ID: "; int idC = Integer.parseInt(scanner.nextLine())
            print "ID Vaga: "; int idV = Integer.parseInt(scanner.nextLine())

            cDAO.registrarCurtida(idC, idV)
            println "Interesse registrado."
        } catch (Exception e) {
            println "Erro: ${e.message}"
        }
    }

    private static void fluxoGerenciarMatches(Scanner scanner, EmpresaDAO eDAO, CandidatoDAO cDAO, EmpresaService eService) {
        try {
            print "ID da sua Empresa: "
            int idEmp = Integer.parseInt(scanner.nextLine())

            println "\n1. Ver Interessados e Curtir | 2. Histórico de Matches"
            print "Escolha: "
            int opt = Integer.parseInt(scanner.nextLine())

            if (opt == 1) {
                def interessados = eDAO.listarInteressados(idEmp)
                if (interessados.isEmpty()) {
                    println "Nenhum interessado no momento."
                    return
                }
                println "\nCandidatos interessados: "
                interessados.each { row ->
                    def candCompleto = cDAO.listarTodos().find { it.id == row.cand_id }
                    println "ID: ${row.cand_id} | Vaga: ${row.vaga_nome}"
                    println "Skills do Candidato: ${candCompleto?.competencias?.join(', ') ?: 'Nenhuma registrada'}"
                }
                print "\nID do Candidato para retribuir curtida (0 para sair): "
                int idCand = Integer.parseInt(scanner.nextLine())

                if (idCand != 0) {
                    eService.gerenciarInteresse(idEmp, idCand, cDAO)
                    def matches = eDAO.listarMatchesReais(idEmp)
                    def candidatoAlvo = cDAO.listarTodos().find { it.id == idCand }

                    if (matches.any { it.candidato == candidatoAlvo?.nome }) {
                        println "MATCH REALIZADO!"
                        candidatoAlvo.exibirPerfil()
                    }
                }
            } else if (opt == 2) {
                eService.mostrarMatchesConfirmados(idEmp)
            }
        } catch (Exception e) {
            println "Erro no fluxo de matches: ${e.message}"
        }
    }

    private static void fluxoCadastroCandidato(Scanner scanner, CandidatoService service) {
        print "Nome: "; String n = scanner.nextLine()
        print "Sobrenome: "; String s = scanner.nextLine()
        print "Email: "; String e = scanner.nextLine()
        print "CPF: "; String c = scanner.nextLine()
        print "Data (YYYY-MM-DD): "; String d = scanner.nextLine()
        print "País: "; String p = scanner.nextLine()
        print "CEP: "; String cp = scanner.nextLine()
        print "Bio: "; String b = scanner.nextLine()
        print "Senha: "; String sn = scanner.nextLine()
        print "Skills: "; List sks = scanner.nextLine().split(',').collect { it.trim() }
        service.registrarNovoCandidato(n, s, d, e, c, p, cp, b, sn, sks)
    }

    private static void fluxoCadastroEmpresa(Scanner scanner, EmpresaService service) {
        print "Nome: "; String n = scanner.nextLine()
        print "CNPJ: "; String cj = scanner.nextLine()
        print "Email: "; String e = scanner.nextLine()
        print "CEP: "; String cp = scanner.nextLine()
        print "País: "; String p = scanner.nextLine()
        print "Bio: "; String d = scanner.nextLine()
        print "Senha: "; String s = scanner.nextLine()
        service.registrarEmpresa(n, e, cj, cp, p, d, s)
    }

    private static void menuGerenciarCompetencias(Scanner scanner, CompetenciaService service) {
        println "\n1. Vincular Skill | 2. Listar Catálogo | 3. Nova Competência | 4. Atualizar Nome | 5. Deletar"
        print "Escolha: "
        int op = Integer.parseInt(scanner.nextLine())

        switch(op) {
            case 1:
                print "ID Candidato: "; int id = Integer.parseInt(scanner.nextLine())
                print "Nome da Skill: "; service.vincularAoCandidato(id, scanner.nextLine())
                break
            case 2:
                service.listarTodas(); break
            case 3:
                print "Nome da nova competência: "; service.salvarCompetencia(scanner.nextLine())
                break
            case 4:
                service.listarTodas()
                print "ID da competência para editar: "; int idAtu = Integer.parseInt(scanner.nextLine())
                print "Novo nome: "; String novoNome = scanner.nextLine()
                service.atualizarCompetencia(idAtu, novoNome)
                break
            case 5:
                service.listarTodas()
                print "ID para deletar: "; int idDel = Integer.parseInt(scanner.nextLine())
                service.excluirCompetencia(idDel)
                break
            default:
                println "Opção inválida."
        }
    }
}