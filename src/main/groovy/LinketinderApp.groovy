import java.util.Scanner

class LinketinderApp {
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in)

        def sql = Conexao.getConn()
        CandidatoDAO candidatoDAO = new CandidatoDAO(sql)
        EmpresaDAO empresaDAO = new EmpresaDAO(sql)
        VagaDAO vagaDAO = new VagaDAO(sql)
        CompetenciaDAO competenciaDAO = new CompetenciaDAO(sql)

        while (true) {
            exibirMenuPrincipal()
            String opcaoInput = scanner.nextLine()
            if (opcaoInput == "0") break

            try {
                int opcao = Integer.parseInt(opcaoInput)
                processarOpcao(opcao, scanner, candidatoDAO, empresaDAO, vagaDAO, competenciaDAO)
            } catch (NumberFormatException e) {
                println "Erro: Insira um número válido."
            }
        }
    }

    private static void exibirMenuPrincipal() {
        println "\nLinketinder - Menu Principal"
        println "1. Gerenciamento Geral (Listar/Atualizar/Deletar)"
        println "2. Área da Empresa (Vagas)"
        println "3. Área do Candidato (Ver Vagas/Curtir)"
        println "4. Gerenciar Matches (Empresa)"
        println "5. Cadastrar Candidato"
        println "6. Cadastrar Empresa"
        println "9. Gerenciar Competências"
        println "0. Sair"
        print "Escolha: "
    }

    private static void processarOpcao(int opcao, Scanner scanner, CandidatoDAO cDAO, EmpresaDAO eDAO, VagaDAO vDAO, CompetenciaDAO compDAO) {
        switch (opcao) {
            case 1: menuGerenciamentoGeral(scanner, cDAO, eDAO); break
            case 2: menuAreaEmpresa(scanner, eDAO, vDAO); break
            case 3: menuAreaCandidato(scanner, cDAO, vDAO); break
            case 4: menuGerenciarMatches(scanner, eDAO, cDAO); break
            case 5: cadastrarCandidato(scanner, cDAO); break
            case 6: cadastrarEmpresa(scanner, eDAO); break
            case 9: menuGerenciarCompetencias(scanner, cDAO, compDAO); break
            default: println "Opção inválida."
        }
    }

    private static void menuGerenciamentoGeral(Scanner scanner, CandidatoDAO cDAO, EmpresaDAO eDAO) {
        println "\nGerenciamento Geral: "
        println "1. Listar Tudo | 2. Atualizar Candidato | 3. Excluir Candidato | 4. Atualizar Empresa | 5. Excluir Empresa"
        print "Escolha: "

        try {
            int opcao = Integer.parseInt(scanner.nextLine())

            if (opcao == 1) {
                println "\nListagem Completa: "
                println "CANDIDATOS:"
                cDAO.listarTodos().each { it.exibirPerfil() }
                println "\nEMPRESAS:"
                eDAO.listarTodas().each { it.exibirPerfil() }

            } else if (opcao == 2) {
                List<Candidato> listaCandidatos = cDAO.listarTodos()
                if (listaCandidatos.isEmpty()) {
                    println "Nenhum candidato cadastrado."
                    return
                }
                listaCandidatos.each { println "[ID: ${it.id}] - ${it.nome} ${it.sobrenome}" }
                print "ID do Candidato para atualizar: "
                int idBusca = Integer.parseInt(scanner.nextLine())
                Candidato candidato = listaCandidatos.find { it.id == idBusca }

                if (candidato) {
                    println "Deixe em branco para manter o valor atual."
                    print "Novo Nome (${candidato.nome}): "; String nome = scanner.nextLine()
                    print "Novo Sobrenome (${candidato.sobrenome}): "; String sobrenome = scanner.nextLine()
                    print "Nova Bio (${candidato.descricao}): "; String bio = scanner.nextLine()
                    print "Novo CEP (${candidato.cep}): "; String cep = scanner.nextLine()
                    print "Novo Email (${candidato.email}): "; String email = scanner.nextLine()
                    print "Novo País (${candidato.pais}): "; String pais = scanner.nextLine()

                    if (!nome.isEmpty()) candidato.nome = nome
                    if (!sobrenome.isEmpty()) candidato.sobrenome = sobrenome
                    if (!bio.isEmpty()) candidato.descricao = bio
                    if (!cep.isEmpty()) candidato.cep = cep
                    if (!email.isEmpty()) candidato.email = email
                    if (!pais.isEmpty()) candidato.pais = pais

                    cDAO.atualizar(candidato)
                    println "Sucesso: Dados do candidato atualizados!"
                } else {
                    println "Candidato não encontrado."
                }

            } else if (opcao == 3) {
                List<Candidato> lista = cDAO.listarTodos()
                lista.each { println "[ID: ${it.id}] - ${it.nome}" }
                print "ID do Candidato para excluir: "
                int idExcluir = Integer.parseInt(scanner.nextLine())
                if (lista.any { it.id == idExcluir }) {
                    cDAO.deletar(idExcluir)
                    println "Sucesso: Candidato removido com sucesso!"
                } else {
                    println "ID inválido."
                }

            } else if (opcao == 4) {
                List<Empresa> listaEmpresas = eDAO.listarTodas()
                if (listaEmpresas.isEmpty()) {
                    println "Nenhuma empresa cadastrada."
                    return
                }
                listaEmpresas.each { println "[ID: ${it.id}] - ${it.nome}" }
                print "ID da Empresa para atualizar: "
                int idBusca = Integer.parseInt(scanner.nextLine())
                Empresa empresa = listaEmpresas.find { it.id == idBusca }

                if (empresa) {
                    println "Deixe em branco para manter o valor atual."
                    print "Novo Nome Fantasia (${empresa.nome}): "; String nome = scanner.nextLine()
                    print "Novo CNPJ (${empresa.cnpj}): "; String cnpj = scanner.nextLine()
                    print "Nova Descrição (${empresa.descricao}): "; String desc = scanner.nextLine()
                    print "Novo Email (${empresa.email}): "; String email = scanner.nextLine()
                    print "Novo CEP (${empresa.cep}): "; String cep = scanner.nextLine()

                    if (!nome.isEmpty()) empresa.nome = nome
                    if (!cnpj.isEmpty()) empresa.cnpj = cnpj
                    if (!desc.isEmpty()) empresa.descricao = desc
                    if (!email.isEmpty()) empresa.email = email
                    if (!cep.isEmpty()) empresa.cep = cep

                    eDAO.atualizar(empresa)
                    println "Sucesso: Dados da empresa atualizados!"
                } else {
                    println "Empresa não encontrada."
                }

            } else if (opcao == 5) {
                List<Empresa> lista = eDAO.listarTodas()
                lista.each { println "[ID: ${it.id}] - ${it.nome}" }
                print "ID da Empresa para excluir: "
                int idExcluir = Integer.parseInt(scanner.nextLine())
                if (lista.any { it.id == idExcluir }) {
                    eDAO.deletar(idExcluir)
                    println "Sucesso: Empresa removida com sucesso!"
                } else {
                    println "ID inválido."
                }
            }
        } catch (NumberFormatException e) {
            println "Erro: Entrada inválida. Digite um número."
        }
    }
    private static void menuAreaEmpresa(Scanner scanner, EmpresaDAO eDAO, VagaDAO vDAO) {
        List<Empresa> empresas = eDAO.listarTodas()
        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada."
            return
        }

        empresas.each { println "[${it.id}] ${it.nome}" }
        print "ID da Empresa: "
        int idEmpresa = Integer.parseInt(scanner.nextLine())
        Empresa logada = empresas.find { it.id == idEmpresa }
        if (!logada) {
            println "Empresa não encontrada."
            return
        }

        println "1. Criar Vaga | 2. Atualizar Vaga | 3. Deletar Vaga | 4. Listar Minhas Vagas"
        print "Escolha: "
        int subOpcao = Integer.parseInt(scanner.nextLine())

        switch (subOpcao) {
            case 1:
                print "Título: "
                String titulo = scanner.nextLine()
                print "Descrição: "
                String desc = scanner.nextLine()
                print "Local: "
                String local = scanner.nextLine()
                print "Requisitos (vírgula): "
                String reqsInput = scanner.nextLine()
                List<String> reqs = reqsInput.split(',').collect { it.trim() }.findAll { !it.empty }

                Vaga novaVaga = new Vaga(titulo, desc, reqs, logada)
                novaVaga.localEstadoCidade = local

                int idVaga = vDAO.salvar(novaVaga, logada.id)
                if (idVaga != -1) {
                    reqs.each { vDAO.vincularRequisito(idVaga, it) }
                    println "Sucesso: Vaga cadastrada!"
                }
                break

            case 2:
                List<Vaga> minhasVagas = vDAO.listarTodas().findAll { it.empresa.id == logada.id }
                if (minhasVagas.isEmpty()) {
                    println "Você não possui vagas cadastradas."
                } else {
                    minhasVagas.each { println "[ID: ${it.id}] ${it.nome}" }
                    print "ID da vaga para editar: "
                    int idEdit = Integer.parseInt(scanner.nextLine())
                    Vaga vEdit = minhasVagas.find { it.id == idEdit }

                    if (vEdit) {
                        println "Deixe em branco para manter o valor atual."
                        print "Novo Titulo (${vEdit.nome}): "
                        String nt = scanner.nextLine()
                        print "Nova Descricao (${vEdit.descricao}): "
                        String nd = scanner.nextLine()
                        print "Novo Local (${vEdit.localEstadoCidade}): "
                        String nl = scanner.nextLine()

                        if (!nt.isEmpty()) vEdit.nome = nt
                        if (!nd.isEmpty()) vEdit.descricao = nd
                        if (!nl.isEmpty()) vEdit.localEstadoCidade = nl

                        vDAO.atualizar(vEdit)
                        println "Sucesso: Vaga atualizada!"
                    }
                }
                break

            case 3:
                List<Vaga> vagasParaDeletar = vDAO.listarTodas().findAll { it.empresa.id == logada.id }
                vagasParaDeletar.each { println "[ID: ${it.id}] ${it.nome}" }
                print "ID da vaga para excluir: "
                int idDel = Integer.parseInt(scanner.nextLine())
                vDAO.deletar(idDel)
                println "Sucesso: Vaga removida!"
                break

            case 4:
                vDAO.listarTodas()
                        .findAll { it.empresa.id == logada.id }
                        .eachWithIndex { vaga, i -> vaga.exibirVaga(i) }
                break

            default:
                println "Opção inválida."
                break
        }
    }
    private static void menuAreaCandidato(Scanner scanner, CandidatoDAO cDAO, VagaDAO vDAO) {
        try {
            List<Candidato> cands = cDAO.listarTodos()
            if (cands.isEmpty()) {
                println "Nenhum candidato cadastrado."
                return
            }

            cands.each { println "[ID: ${it.id}] - ${it.nome}" }
            print "Seu ID de Candidato: "
            int idLogado = Integer.parseInt(scanner.nextLine())
            Candidato logado = cands.find { it.id == idLogado }

            if (!logado) {
                println "Candidato não encontrado."
                return
            }

            List<Vaga> vagas = vDAO.listarTodas()
            if (vagas.isEmpty()) {
                println "Não há vagas disponíveis no momento."
                return
            }

            println "\nVagas Disponíveis: "
            vagas.each { v ->
                println "ID: ${v.id} | Cargo: ${v.nome} | Empresa: ${v.empresa.nome}"
                println "Requisitos: ${v.requisitos.join(', ')}"
                println "------------------------------------------"
            }

            print "Digite o ID da vaga para curtir (ou 0 para voltar): "
            int idVaga = Integer.parseInt(scanner.nextLine())

            if (idVaga != 0) {
                if (vagas.any { it.id == idVaga }) {
                    cDAO.curtirVaga(logado.id, idVaga)
                } else {
                    println "Erro: ID de vaga inválido."
                }
            }
        } catch (NumberFormatException e) {
            println "Erro: Digite apenas números para IDs."
        } catch (Exception e) {
            println "Erro na operação: ${e.message}"
        }
    }
    private static void menuGerenciarMatches(Scanner scanner, EmpresaDAO eDAO, CandidatoDAO cDAO) {
        try {
            List<Empresa> empresas = eDAO.listarTodas()
            if (empresas.isEmpty()) {
                println "Nenhuma empresa cadastrada no sistema."
                return
            }

            println "\nSelecione a Empresa: "
            empresas.each { println "[ID: ${it.id}] - ${it.nome}" }
            print "ID da Empresa: "
            int idEmp = Integer.parseInt(scanner.nextLine())

            Empresa empresaLogada = empresas.find { it.id == idEmp }
            if (!empresaLogada) {
                println "Empresa não encontrada."
                return
            }

            println "\n1. Ver Interessados e Curtir | 2. Ver Matches Reais"
            print "Escolha: "
            int opt = Integer.parseInt(scanner.nextLine())

            if (opt == 1) {
                def interessados = eDAO.listarInteressados(idEmp)
                if (interessados.isEmpty()) {
                    println "\nNenhum candidato interessado nas suas vagas no momento."
                    return
                }

                println "\nCandidatos Interessados: "
                interessados.each { row ->
                    def candidato = cDAO.listarTodos().find { it.id == row.cand_id }
                    String competencias = candidato?.competencias ? candidato.competencias.join(', ') : "Nenhuma informada"

                    println "ID Candidato: ${row.cand_id}"
                    println "Vaga: [ID: ${row.vaga_id}] ${row.vaga_nome}"
                    println "Competências: ${competencias}"
                }

                print "\nDigite o ID do Candidato para curtir (0 para sair): "
                int idC = Integer.parseInt(scanner.nextLine())

                if (idC != 0) {
                    eDAO.curtirCandidato(idEmp, idC)

                    Candidato curtido = cDAO.listarTodos().find { it.id == idC }

                    if (curtido) {
                        println "MATCH REALIZADO!"
                        curtido.exibirPerfil()
                        println "Contato: ${curtido.email}"
                    } else {
                        println "Sucesso: Curtida registrada!"
                    }
                }
            } else if (opt == 2) {
                def matches = eDAO.listarMatchesReais(idEmp)
                if (matches.isEmpty()) {
                    println "\nAinda não há matches confirmados."
                } else {
                    println "\nSeus Matches: "
                    matches.each { println "MATCH: ${it.candidato} <-> Vaga: ${it.vaga}" }
                }
            }
        } catch (NumberFormatException e) {
            println "Erro: Insira um ID numérico válido."
        } catch (Exception e) {
            println "Erro na operação de matches: ${e.message}"
        }
    }
    private static void cadastrarCandidato(Scanner scanner, CandidatoDAO cDAO) {
        print "Nome: "; String nome = scanner.nextLine()
        print "Sobrenome: "; String sobrenome = scanner.nextLine()
        print "Email: "; String email = scanner.nextLine()
        print "CPF: "; String cpf = scanner.nextLine()
        print "Data Nascimento: "; String data = scanner.nextLine()
        print "País: "; String pais = scanner.nextLine()
        print "CEP: "; String cep = scanner.nextLine()
        print "Bio: "; String bio = scanner.nextLine()
        print "Senha: "; String senha = scanner.nextLine()
        print "Skills (virgula): "; String skInput = scanner.nextLine()

        List<String> skills = skInput.split(',').collect { it.trim() }.findAll { !it.empty }

        Candidato c = new Candidato(nome, email, pais, cep, bio, cpf, 0, skills)
        c.sobrenome = sobrenome
        c.dataNascimento = data
        c.senha = senha
        c.pais = pais

        int id = cDAO.salvar(c)
        if (id != -1) {
            skills.each { cDAO.vincularCompetencia(id, it) }
            println "Sucesso: Candidato cadastrado com ID $id!"
        }
    }
    private static void cadastrarEmpresa(Scanner scanner, EmpresaDAO eDAO) {
        print "Nome: "; String nome = scanner.nextLine()
        print "CNPJ: "; String cnpj = scanner.nextLine()
        print "Email: "; String email = scanner.nextLine()
        print "Cep: "; String cep = scanner.nextLine()
        print "Pais: "; String pais = scanner.nextLine()
        print "Descrição: "; String descricao = scanner.nextLine()
        Empresa e = new Empresa(nome, email, "", cep, descricao, cnpj, pais, [])
        eDAO.salvar(e)
    }

    private static void menuGerenciarCompetencias(Scanner scanner, CandidatoDAO cDAO, CompetenciaDAO compDAO) {
        println "1. Perfil Candidato (Vincular) | 2. Catálogo Geral (Gerenciar)"
        int op = Integer.parseInt(scanner.nextLine())

        if (op == 1) {
            print "Seu ID: "; int id = Integer.parseInt(scanner.nextLine())
            print "Nome da Skill: "; cDAO.vincularCompetencia(id, scanner.nextLine())
        } else {
            println "1. Listar | 2. Atualizar | 3. Deletar | 4. Criar Nova"
            int subOp = Integer.parseInt(scanner.nextLine())

            switch(subOp) {
                case 1:
                    compDAO.listar().each { println "ID: ${it.id} | Nome: ${it.nome}" }; break
                case 2:
                    print "ID da competência: "; int idAtu = Integer.parseInt(scanner.nextLine())
                    print "Novo nome: "; String novo = scanner.nextLine()
                    compDAO.atualizar(idAtu, novo); break
                case 3:
                    print "ID para deletar: "; compDAO.deletar(Integer.parseInt(scanner.nextLine())); break
                case 4:
                    print "Nova Competência: "; compDAO.salvar(scanner.nextLine()); break
            }
        }
    }
}