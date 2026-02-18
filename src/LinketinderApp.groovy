import java.util.Scanner

class LinketinderApp {
    static void main(String[] args) {
        List<Candidato> candidatos = [
                new Candidato("Alice Silva", "alice@email.com", "BA", "44000-000", "Desenvolvedora Java Fullstack", "123.456.789-00", 22, ["Java", "Spring Boot", "SQL"]),
                new Candidato("Bruno Costa", "bruno@email.com", "SP", "01000-000", "Especialista em Python e Dados", "234.567.890-11", 25, ["Python", "Flask", "Pandas"]),
                new Candidato("Carla Souza", "carla@email.com", "RJ", "20000-000", "Designer UX/UI e Front-end", "345.678.901-22", 21, ["Angular", "TypeScript", "Figma"]),
                new Candidato("Daniel Oliveira", "daniel@email.com", "MG", "30000-000", "Desenvolvedor Mobile Flutter", "456.789.012-33", 23, ["Flutter", "Dart", "Firebase"]),
                new Candidato("Eduardo Lima", "edu@email.com", "BA", "44050-000", "Estudante de Sistemas de Informação", "567.890.123-44", 20, ["Groovy", "Git", "Docker"])
        ]

        List<Empresa> empresas = [
                new Empresa("ZG Soluções", "talentos@zg.com", "GO", "74000-000", "Fintech focada em automação financeira", "77.888.999/0001-00", "Brasil", ["Java", "Groovy", "SQL"]),
                new Empresa("Tech Solutions", "hr@tech.com", "SP", "04571-010", "Consultoria de software global", "11.222.333/0001-44", "Brasil", ["Python", "AWS", "Node.js"]),
                new Empresa("Arroz Gostoso", "rh@arroz.com", "BA", "44010-000", "Indústria de alimentos e logística", "12.345.678/0001-90", "Brasil", ["PHP", "Laravel", "MySQL"]),
                new Empresa("Inova App", "vagas@inova.com", "SC", "88000-000", "Startup de inovação mobile", "55.444.333/0001-22", "Brasil", ["Flutter", "Swift", "Kotlin"]),
                new Empresa("Global Tech", "jobs@global.com", "EUA", "90210", "Empresa de infraestrutura tecnológica", "99.000.111/0001-88", "EUA", ["C#", ".NET", "Azure"])
        ]

        List<Vaga> vagas = []
        List<Curtida> listaCurtidas = []
        Scanner scanner = new Scanner(System.in)
        int opcao = 0

        while (opcao != 8) {
            println "\nLinketinder: "
            println "1. Listar Candidatos / Empresas"
            println "2. Empresa - Gerenciar Vagas (Criar/Editar/Deletar)"
            println "3. Candidato - Ver Vagas e Curtir"
            println "4. Empresa - Ver Interessados e Dar Match"
            println "5. Cadastrar Novo Candidato"
            println "6. Cadastrar Nova Empresa"
            println "7. Ver Matches Realizados"
            println "8. Sair"
            print "Escolha: "

            try {
                opcao = Integer.parseInt(scanner.nextLine())
            } catch (Exception e) {
                continue
            }

            switch (opcao) {
                case 1:
                    println "\nLISTA DE CANDIDATOS: "
                    candidatos.eachWithIndex { c, i -> println "[$i] ${c.nome} | Skills: ${c.competencias}" }
                    println "\nLISTA DE EMPRESAS: "
                    empresas.eachWithIndex { e, i -> println "[$i] ${e.nome} | Local: ${e.pais}" }
                    break

                case 2:
                    println "Índice da Empresa que está operando: "
                    int eIdx = Integer.parseInt(scanner.nextLine())
                    Empresa emp = empresas[eIdx]
                    println "1. Criar Vaga | 2. Editar Vaga | 3. Deletar Vaga"
                    int sub = Integer.parseInt(scanner.nextLine())

                    if (sub == 1) {
                        print "Título da Vaga: "; String t = scanner.nextLine()
                        print "Descrição: "; String d = scanner.nextLine()
                        print "Requisitos (vírgula): "; List r = scanner.nextLine().split(',').collect { it.trim() }
                        vagas.add(new Vaga(t, d, r, emp))
                        println "Sucesso: Vaga criada!"
                    } else {
                        def minhasVagas = vagas.findAll { it.empresa == emp }
                        if (minhasVagas.isEmpty()) { println "Nenhuma vaga encontrada."; break }
                        minhasVagas.eachWithIndex { v, i -> println "[$i] ${v.nome}" }
                        print "Selecione o índice da vaga: "; int vSel = Integer.parseInt(scanner.nextLine())
                        Vaga vagaSel = minhasVagas[vSel]

                        if (sub == 2) {
                            print "Novo Título (vazio para manter): "; String nt = scanner.nextLine()
                            print "Nova Descrição (vazio para manter): "; String nd = scanner.nextLine()
                            if (!nt.isEmpty()) vagaSel.nome = nt
                            if (!nd.isEmpty()) vagaSel.descricao = nd
                            println "Sucesso: Vaga editada!"
                        } else if (sub == 3) {
                            vagas.remove(vagaSel)
                            println "Sucesso: Vaga deletada!"
                        }
                    }
                    break

                case 3:
                    println "Índice do Candidato que está operando: "
                    int cIdx = Integer.parseInt(scanner.nextLine())
                    if (vagas.isEmpty()) { println "Não há vagas abertas no momento."; break }
                    vagas.eachWithIndex { v, i -> v.exibirVaga(i) }
                    print "Índice da vaga para curtir: "
                    int vIdx = Integer.parseInt(scanner.nextLine())
                    listaCurtidas.add(new Curtida(candidatos[cIdx], vagas[vIdx]))
                    println "Interesse registrado anonimamente para a empresa!"
                    break

                case 4:
                    println "Índice da Empresa que está operando: "
                    int empIdx = Integer.parseInt(scanner.nextLine())
                    def interessados = listaCurtidas.findAll { it.vaga.empresa == empresas[empIdx] }
                    if (interessados.isEmpty()) { println "Nenhum candidato curtiu suas vagas ainda."; break }
                    interessados.eachWithIndex { c, i -> c.exibirParaEmpresa(i) }
                    print "Índice para dar Match e revelar dados (ou -1): "
                    int mIdx = Integer.parseInt(scanner.nextLine())
                    if (mIdx != -1) {
                        interessados[mIdx].empresaCurtiu = true
                        println "MATCH! Agora você tem acesso aos dados de contato."
                    }
                    break

                case 5:
                    println "\nCADASTRO DE CANDIDATO: "
                    print "Nome: "; String n = scanner.nextLine()
                    print "E-mail: "; String em = scanner.nextLine()
                    print "CPF: "; String cp = scanner.nextLine()
                    print "Idade: "; int id = Integer.parseInt(scanner.nextLine())
                    print "Estado: "; String st = scanner.nextLine()
                    print "CEP: "; String ce = scanner.nextLine()
                    print "Descrição Pessoal: "; String de = scanner.nextLine()
                    print "Competências (vírgula): "; List sk = scanner.nextLine().split(',').collect { it.trim() }
                    candidatos.add(new Candidato(n, em, st, ce, de, cp, id, sk))
                    break

                case 6:
                    println "\nCADASTRO DE EMPRESA: "
                    print "Nome: "; String ne = scanner.nextLine()
                    print "E-mail Corporativo: "; String ec = scanner.nextLine()
                    print "CNPJ: "; String cj = scanner.nextLine()
                    print "País: "; String pa = scanner.nextLine()
                    print "Estado: "; String es = scanner.nextLine()
                    print "CEP: "; String cpE = scanner.nextLine()
                    print "Descrição da Empresa: "; String deE = scanner.nextLine()
                    print "Requisitos (vírgula): "; List re = scanner.nextLine().split(',').collect { it.trim() }
                    empresas.add(new Empresa(ne, ec, es, cpE, deE, cj, pa, re))
                    break

                case 7:
                    println "\nMatches: "
                    listaCurtidas.findAll { it.isMatch() }.each {
                        println "CONEXÃO: ${it.candidato.nome}, ${it.candidato.email}, ${it.candidato.cpf} <-> Vaga: ${it.vaga.nome} em ${it.vaga.empresa.nome}"
                    }
                    break
            }
        }
    }
}