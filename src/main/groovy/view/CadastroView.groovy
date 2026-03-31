package view

class CadastroView {
    private Scanner scanner = new Scanner(System.in)

    Map coletarDadosCandidato(){
        try{
            println "Formulário de Cadastro: "
            print "Nome: "
            String n = scanner.nextLine()
            print "Sobrenome: "
            String s = scanner.nextLine()
            print "Email: "
            String e = scanner.nextLine()
            print "CPF: "
            String c = scanner.nextLine()
            print "Data Nascimento (YYYY-MM-DD): "
            String d = scanner.nextLine()
            print "País: "
            String p = scanner.nextLine()
            print "CEP: "
            String cp = scanner.nextLine()
            print "Bio: "
            String b = scanner.nextLine()
            print "Senha: "
            String sn = scanner.nextLine()
            print "Skills (separadas por vírgula): "
            List sks = scanner.nextLine().split(',').collect { it.trim() }.findAll { !it.isEmpty() }

            return [nome: n, sobrenome: s, email: e, cpf: c, data: d, pais: p, cep: cp, bio: b, senha: sn, skills: sks, valido: true]
        } catch (Exception e){
            return [valido: false]
        }
    }

    Map coletarDadosEmpresa(){
        try {
            println "Novo Cadastro - Empresa: "
            print "Nome: "
            String n = scanner.nextLine()
            print "CNPJ: "
            String cj = scanner.nextLine()
            print "E-mail Corporativo: "
            String e = scanner.nextLine()
            print "CEP: "
            String cp = scanner.nextLine()
            print "País: "
            String p = scanner.nextLine()
            print "Descrição da Empresa: "
            String d = scanner.nextLine()
            print "Senha: "
            String s = scanner.nextLine()

            return [nome: n, cnpj: cj, email: e, cep: cp, pais: p, desc: d, senha: s, valido: true]
        } catch (Exception e){
            return [valido: false]
        }
    }
}