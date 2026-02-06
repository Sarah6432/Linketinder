class LinketinderApp {
    static void main(String[] args) {

        List<Candidato> candidatos = [
                new Candidato("Alice Silva", "alice@email.com", "BA", "44000-000", "Dev Java apaixonada por backend", "123.456.789-00", 22, ["Java", "Spring Boot"]),
                new Candidato("Bruno Costa", "bruno@email.com", "SP", "01000-000", "Entusiasta de Python e IA", "234.567.890-11", 25, ["Python", "Flask"]),
                new Candidato("Carla Souza", "carla@email.com", "RJ", "20000-000", "Front-end Developer focada em UX", "345.678.901-22", 21, ["Angular", "TypeScript"]),
                new Candidato("Daniel Oliveira", "daniel@email.com", "MG", "30000-000", "Mobile Dev focado em Flutter", "456.789.012-33", 23, ["Flutter", "Dart"]),
                new Candidato("Eduardo Lima", "edu@email.com", "BA", "44050-000", "Estudante de Sistemas de Informação", "567.890.123-44", 20, ["Groovy", "SQL"])
        ]

        List<Empresa> empresas = [
                new Empresa("Arroz-Gostoso", "rh@arrozgostoso.com", "BA", "44010-000", "Líder em alimentos", "12.345.678/0001-90", "Brasil", ["Java", "SQL"]),
                new Empresa("Império do Boliche", "vagas@imperio.com", "SP", "02000-000", "Entretenimento familiar", "98.765.432/0001-10", "Brasil", ["Python", "Flask"]),
                new Empresa("TechSolutions", "contact@tech.com", "EUA", "90210", "Startup de inovação", "11.222.333/0001-44", "EUA", ["Angular", "Node.js"]),
                new Empresa("VarejoHub", "jobs@varejohub.com", "SC", "88000-000", "Soluções para varejo", "55.444.333/0001-22", "Brasil", ["Groovy", "Spring"]),
                new Empresa("ZG Soluções", "talentos@zg.com", "GO", "74000-000", "Inovação financeira", "77.888.999/0001-00", "Brasil", ["Java", "Groovy", "Angular"])
        ]

        Scanner scanner = new Scanner(System.in)
        int opcao = 0

        while (opcao != 3) {
            println "Bem-vindo ao Linketinder"
            println "1. Listar Candidatos"
            println "2. Listar Empresas"
            println "3. Sair"
            print "Escolha uma opção: "

            try {
                opcao = scanner.nextInt()
                println ""

                switch (opcao) {
                    case 1:
                        candidatos.each { it.exibirPerfil() }
                        break
                    case 2:
                        empresas.each { it.exibirPerfil() }
                        break
                    case 3:
                        println "Saindo..."
                        break
                    default:
                        println "Opção inválida!"
                }
            } catch (Exception e) {
                println "Erro: Por favor, digite um número."
                scanner.next()
            }
        }
    }
}