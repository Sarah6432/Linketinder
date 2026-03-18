import java.util.Scanner

class LinketinderApp {
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in)
        CandidatoDAO candidatoDAO = new CandidatoDAO()
        EmpresaDAO empresaDAO = new EmpresaDAO()
        VagaDAO vagaDAO = new VagaDAO()
        CompetenciaDAO competenciaDAO = new CompetenciaDAO()

        while (true) {
            println "\nLINKETINDER - MENU PRINCIPAL"
            println "1. Gerenciamento Geral (Listar/Atu/Del)"
            println "2. Area da Empresa (Vagas)"
            println "3. Area do Candidato (Ver Vagas/Curtir)"
            println "4. Gerenciar Matches (Empresa)"
            println "5. Cadastrar Candidato"
            println "6. Cadastrar Empresa"
            println "9. Gerenciar Competencias"
            println "0. Sair"
            print "Escolha: "

            String opcaoInput = scanner.nextLine()
            if (opcaoInput == "0") break

            int opcao = Integer.parseInt(opcaoInput)

            switch (opcao) {
                case 1:
                    println "\nGERENCIAMENTO"
                    println "1. Listar Tudo"
                    println "2. Atualizar Candidato"
                    println "3. Excluir Candidato"
                    println "4. Atualizar Empresa"
                    println "5. Excluir Empresa"
                    println "6. Voltar"
                    print "Escolha: "
                    int opGerenciar = Integer.parseInt(scanner.nextLine())

                    if (opGerenciar == 1) {
                        println "\nCANDIDATOS"
                        candidatoDAO.listarTodos().each { it.exibirPerfil() }
                        println "\nEMPRESAS"
                        empresaDAO.listarTodas().each { it.exibirPerfil() }
                    } else if (opGerenciar == 2) {
                        List<Candidato> listaCands = candidatoDAO.listarTodos()
                        listaCands.each { println "[ID: ${it.id}] - ${it.nome}" }
                        print "ID para atualizar: "; int idAtu = Integer.parseInt(scanner.nextLine())
                        Candidato cAtu = listaCands.find { it.id == idAtu }
                        if (cAtu) {
                            print "Novo Nome (${cAtu.nome}): "; String n = scanner.nextLine()
                            print "Nova Bio (${cAtu.descricao}): "; String d = scanner.nextLine()
                            print "Novo CEP (${cAtu.cep}): "; String ce = scanner.nextLine()
                            if (!n.isEmpty()) cAtu.nome = n
                            if (!d.isEmpty()) cAtu.descricao = d
                            if (!ce.isEmpty()) cAtu.cep = ce
                            candidatoDAO.atualizar(cAtu)
                        }
                    } else if (opGerenciar == 3) {
                        candidatoDAO.listarTodos().each { println "[ID: ${it.id}] - ${it.nome}" }
                        print "ID para excluir: "; int idDel = Integer.parseInt(scanner.nextLine())
                        candidatoDAO.deletar(idDel)
                    } else if (opGerenciar == 4) {
                        List<Empresa> listaEmps = empresaDAO.listarTodas()
                        listaEmps.each { println "[ID: ${it.id}] - ${it.nome}" }
                        print "ID da Empresa para atualizar: "; int idEmpAtu = Integer.parseInt(scanner.nextLine())
                        Empresa eAtu = listaEmps.find { it.id == idEmpAtu }
                        if (eAtu) {
                            print "Novo Nome (${eAtu.nome}): "; String n = scanner.nextLine()
                            print "Nova Descricao (${eAtu.descricao}): "; String d = scanner.nextLine()
                            if (!n.isEmpty()) eAtu.nome = n
                            if (!d.isEmpty()) eAtu.descricao = d
                            empresaDAO.atualizar(eAtu)
                        }
                    } else if (opGerenciar == 5) {
                        empresaDAO.listarTodas().each { println "[ID: ${it.id}] - ${it.nome}" }
                        print "ID da Empresa para excluir: "; int idEmpDel = Integer.parseInt(scanner.nextLine())
                        empresaDAO.deletar(idEmpDel)
                    }
                    break

                case 2:
                    List<Empresa> empsLocal = empresaDAO.listarTodas()
                    if (empsLocal.isEmpty()) {
                        println "Nenhuma empresa cadastrada."
                        break
                    }
                    empsLocal.each { println "[${it.id}] ${it.nome}" }
                    print "ID da Empresa: "; int eId = Integer.parseInt(scanner.nextLine())
                    Empresa empLogada = empsLocal.find { it.id == eId }
                    if (!empLogada) break

                    println "1. Criar Vaga | 2. Atualizar Vaga | 3. Deletar Vaga | 4. Listar Minhas Vagas"
                    print "Escolha: "
                    int subOp = Integer.parseInt(scanner.nextLine())

                    if (subOp == 1) {
                        print "Titulo: "; String t = scanner.nextLine()
                        print "Descricao: "; String d = scanner.nextLine()
                        print "Local: "; String loc = scanner.nextLine()
                        print "Requisitos (virgula): "; String reqInput = scanner.nextLine()
                        List<String> listaReqs = reqInput.split(',').collect { it.trim() }.findAll { !it.isEmpty() }

                        Vaga nv = new Vaga(t, d, listaReqs, empLogada)
                        nv.localEstadoCidade = loc

                        int idVaga = vagaDAO.salvar(nv, empLogada.id)
                        if (idVaga != -1) listaReqs.each { vagaDAO.vincularRequisito(idVaga, it) }
                        println "Vaga cadastrada com sucesso."

                    } else if (subOp == 2) {
                        List<Vaga> minhasVagas = vagaDAO.listarTodas().findAll { it.empresa.id == empLogada.id }
                        if (minhasVagas.isEmpty()) {
                            println "Nenhuma vaga encontrada."
                        } else {
                            minhasVagas.each { println "[ID: ${it.id}] ${it.nome}" }
                            print "ID da vaga para editar: "; int idEdit = Integer.parseInt(scanner.nextLine())
                            Vaga vEdit = minhasVagas.find { it.id == idEdit }

                            if (vEdit) {
                                println "Deixe em branco para manter o valor atual."
                                print "Novo Titulo (${vEdit.nome}): "; String nt = scanner.nextLine()
                                print "Nova Descricao (${vEdit.descricao}): "; String nd = scanner.nextLine()
                                print "Novo Local (${vEdit.localEstadoCidade}): "; String nl = scanner.nextLine()

                                if (!nt.isEmpty()) vEdit.nome = nt
                                if (!nd.isEmpty()) vEdit.descricao = nd
                                if (!nl.isEmpty()) vEdit.localEstadoCidade = nl

                                vagaDAO.atualizar(vEdit)
                                println "Vaga atualizada com sucesso."
                            }
                        }

                    } else if (subOp == 3) {
                        vagaDAO.listarTodas().findAll { it.empresa.id == empLogada.id }.each { println "[ID: ${it.id}] ${it.nome}" }
                        print "ID para deletar: "; int idVd = Integer.parseInt(scanner.nextLine())
                        vagaDAO.deletar(idVd)
                        println "Vaga removida."

                    } else if (subOp == 4) {
                        vagaDAO.listarTodas().findAll { it.empresa.id == empLogada.id }.eachWithIndex { v, i -> v.exibirVaga(i) }
                    }
                    break

                case 3:
                    List<Candidato> cands = candidatoDAO.listarTodos()
                    if (cands.isEmpty()) break
                    cands.each { println "[ID: ${it.id}] - ${it.nome}" }
                    print "Seu ID: "; int idLog = Integer.parseInt(scanner.nextLine())
                    Candidato candLog = cands.find { it.id == idLog }
                    if (!candLog) break

                    List<Vaga> vags = vagaDAO.listarTodas()
                    vags.each { v ->
                        println "VAGA ID: ${v.id} | ${v.nome} | DESC: ${v.descricao} | REQ: ${v.requisitos.join(', ')}"
                    }
                    print "ID da vaga para curtir (0 para sair): "
                    int idVcurte = Integer.parseInt(scanner.nextLine())
                    if (idVcurte != 0) candidatoDAO.curtirVaga(candLog.id, idVcurte)
                    break

                case 4:
                    List<Empresa> empsMatch = empresaDAO.listarTodas()
                    if (empsMatch.isEmpty()) break
                    empsMatch.each { println "[ID: ${it.id}] - ${it.nome}" }
                    print "ID da Empresa: "; int idEmpM = Integer.parseInt(scanner.nextLine())

                    println "1. Interessados | 2. Ver Matches"
                    int optM = Integer.parseInt(scanner.nextLine())
                    if (optM == 1) {
                        def interessados = empresaDAO.listarInteressados(idEmpM)
                        if (interessados.isEmpty()) { println "Sem interessados."; break }
                        interessados.each { row ->
                            def candObj = candidatoDAO.listarTodos().find { it.id == row.cand_id }
                            println "ID: ${row.cand_id} | Skills: ${candObj?.competencias?.join(', ')} | Vaga: ${row.vaga_nome}"
                        }
                        print "ID do Candidato para curtir (0 sair): "; int cIdM = Integer.parseInt(scanner.nextLine())
                        if (cIdM != 0) {
                            empresaDAO.curtirCandidato(idEmpM, cIdM)
                            def matches = empresaDAO.listarMatchesReais(idEmpM)
                            def dadosC = interessados.find { it.cand_id == cIdM }
                            if (dadosC && matches.any { it.vaga == dadosC.vaga_nome }) println "MATCH REALIZADO!"
                        }
                    } else if (optM == 2) {
                        empresaDAO.listarMatchesReais(idEmpM).each { println "Candidato: ${it.candidato} | Vaga: ${it.vaga}" }
                    }
                    break

                case 5:
                    print "Nome: "; String n = scanner.nextLine()
                    print "Sobrenome: "; String sn = scanner.nextLine()
                    print "Email: "; String em = scanner.nextLine()
                    print "CPF: "; String cp = scanner.nextLine()
                    print "Nascimento (yyyy-mm-dd): "; String dt = scanner.nextLine()
                    print "Pais: "; String p = scanner.nextLine()
                    print "CEP: "; String ce = scanner.nextLine()
                    print "Descricao: "; String de = scanner.nextLine()
                    print "Senha: "; String pw = scanner.nextLine()
                    print "Skills (virgula): "; String skInput = scanner.nextLine()
                    List<String> sks = skInput.split(',').collect { it.trim() }
                    Candidato nc = new Candidato(n, em, p, ce, de, cp, 0, sks)
                    nc.sobrenome = sn; nc.dataNascimento = dt; nc.pais = p; nc.senha = pw
                    int idC = candidatoDAO.salvar(nc)
                    if (idC != -1) sks.each { candidatoDAO.vincularCompetencia(idC, it) }
                    break

                case 6:
                    print "Empresa: "; String ne = scanner.nextLine()
                    print "CNPJ: "; String cj = scanner.nextLine()
                    print "Email: "; String ee = scanner.nextLine()
                    print "Descricao: "; String dce = scanner.nextLine()
                    print "Pais: "; String pe = scanner.nextLine()
                    print "CEP: "; String cpe = scanner.nextLine()
                    print "Senha: "; String se = scanner.nextLine()
                    Empresa nemp = new Empresa(ne, ee, pe, cpe, dce, cj, pe, [])
                    nemp.senha = se
                    empresaDAO.salvar(nemp)
                    break

                case 9:
                    println "1. Candidato (Meu Perfil) | 2. Empresa (Catalogo)"
                    int tP = Integer.parseInt(scanner.nextLine())
                    if (tP == 1) {
                        candidatoDAO.listarTodos().each { println "[ID: ${it.id}] - ${it.nome}" }
                        print "Seu ID: "; int idC = Integer.parseInt(scanner.nextLine())
                        println "1. Add Skill | 2. Del Skill"
                        int oC = Integer.parseInt(scanner.nextLine())
                        if (oC == 1) {
                            print "Nome: "; candidatoDAO.vincularCompetencia(idC, scanner.nextLine())
                        } else if (oC == 2) {
                            print "Nome: "; candidatoDAO.desvincularCompetencia(idC, scanner.nextLine())
                        }
                    } else if (tP == 2) {
                        println "1. Criar | 2. Editar | 3. Deletar"
                        int oE = Integer.parseInt(scanner.nextLine())
                        if (oE == 1) {
                            print "Nome: "; competenciaDAO.salvar(scanner.nextLine())
                        } else if (oE == 2) {
                            competenciaDAO.listar().each { println "[ID: ${it.id}] ${it.nome}" }
                            print "ID: "; int idEd = Integer.parseInt(scanner.nextLine())
                            print "Novo: "; competenciaDAO.atualizar(idEd, scanner.nextLine())
                        } else if (oE == 3) {
                            competenciaDAO.listar().each { println "[ID: ${it.id}] ${it.nome}" }
                            print "ID: "; competenciaDAO.deletar(Integer.parseInt(scanner.nextLine()))
                        }
                    }
                    break
            }
        }
    }
}