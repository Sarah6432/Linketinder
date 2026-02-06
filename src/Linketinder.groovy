/**
 * Linketinder - ZG-Hero Project
 * Desenvolvedora: Sarah Silva Lima
 * Versão: 1.0 (Sistema de Match)
 */

class LinketinderApp {
    static void main(String[] args) {
        List<Candidato> candidatos = [
                new Candidato("Alice Silva", "alice@email.com", "BA", "44000-000", "Dev Java", "123.456.789-00", 22, ["Java", "Spring Boot"]),
                new Candidato("Bruno Costa", "bruno@email.com", "SP", "01000-000", "Pythonista", "234.567.890-11", 25, ["Python", "Flask"]),
                new Candidato("Carla Souza", "carla@email.com", "RJ", "20000-000", "Front-end", "345.678.901-22", 21, ["Angular", "TypeScript"]),
                new Candidato("Daniel Oliveira", "daniel@email.com", "MG", "30000-000", "Mobile", "456.789.012-33", 23, ["Flutter", "Dart"]),
                new Candidato("Eduardo Lima", "edu@email.com", "BA", "44050-000", "Sistemas", "567.890.123-44", 20, ["Groovy", "SQL"])
        ]

        List<Empresa> empresas = [
                new Empresa("Arroz-Gostoso", "rh@arrozgostoso.com", "BA", "44010-000", "Alimentos", "12.345.678/0001-90", "Brasil", ["Java", "SQL"]),
                new Empresa("Império do Boliche", "vagas@imperio.com", "SP", "02000-000", "Lazer", "98.765.432/0001-10", "Brasil", ["Python", "Flask"]),
                new Empresa("TechSolutions", "contact@tech.com", "EUA", "90210", "Inovação", "11.222.333/0001-44", "EUA", ["Angular", "Node.js"]),
                new Empresa("VarejoHub", "jobs@varejohub.com", "SC", "88000-000", "Varejo", "55.444.333/0001-22", "Brasil", ["Groovy", "Spring"]),
                new Empresa("ZG Soluções", "talentos@zg.com", "GO", "74000-000", "Fintech", "77.888.999/0001-00", "Brasil", ["Java", "Groovy"])
        ]

        List<Curtida> listaCurtidas = []
        Scanner scanner = new Scanner(System.in)
        int opcao = 0

        while (opcao != 5) {
            println "Bem-vindo!"
            println "1. Listar Candidatos"
            println "2. Listar Empresas"
            println "3. Candidato: Curtir uma Empresa"
            println "4. Empresa: Curtir um Candidato"
            println "5. Sair"
            print "Escolha: "

            try {
                opcao = scanner.nextInt()
            } catch (Exception e) {
                println "Entrada inválida!"
                scanner.next()
                continue
            }

            switch (opcao) {
                case 1:
                    println "\nLISTA DE CANDIDATOS"
                    candidatos.eachWithIndex { c, i -> println "[$i] ${c.nome} - Skills: ${c.competencias}" }
                    break

                case 2:
                    println "\nLISTA DE EMPRESAS"
                    empresas.eachWithIndex { e, i -> println "[$i] ${e.nome} - Requisitos: ${e.competencias}" }
                    break

                case 3:
                    println "Digite o índice do CANDIDATO: "
                    int cIdx = scanner.nextInt()
                    println "Digite o índice da EMPRESA que deseja curtir: "
                    int eIdx = scanner.nextInt()
                    registrarInteracao(listaCurtidas, candidatos[cIdx], empresas[eIdx], true)
                    break

                case 4:
                    println "Digite o índice da EMPRESA: "
                    int eIdx = scanner.nextInt()
                    println "Digite o índice do CANDIDATO que deseja curtir: "
                    int cIdx = scanner.nextInt()
                    registrarInteracao(listaCurtidas, candidatos[cIdx], empresas[eIdx], false)
                    break

                case 5:
                    println "Finalizando sistema... Até logo!"
                    break
            }
        }
    }

    static void registrarInteracao(List<Curtida> lista, Candidato c, Empresa e, boolean ehCandidato) {
        Curtida interacao = lista.find { it.candidato == c && it.empresa == e }

        if (!interacao) {
            interacao = new Curtida(c, e)
            lista.add(interacao)
        }

        if (ehCandidato) {
            interacao.candidatoCurtiu = true
            println "\nSucesso: O candidato ${c.nome} demonstrou interesse na ${e.nome}!"
        } else {
            interacao.empresaCurtiu = true
            println "\nSucesso: A empresa ${e.nome} demonstrou interesse em ${c.nome}!"
        }

        if (interacao.isMatch()) {
            println "MATCH DETECTADO!"
            println "O candidato ${c.nome} e a empresa ${e.nome} se curtiram!"
        }
    }
}