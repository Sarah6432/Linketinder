package view

import controller.CadastroController

class CadastroView {
    private Scanner scanner = new Scanner(System.in)
    private CadastroController controller

    CadastroView(CadastroController controller) {
        this.controller = controller
    }

    void exibirMenuCadastro() {
        println "\nNovo Cadastro:"
        println "1. Candidato"
        println "2. Empresa"
        println "0. Voltar"
        print "Escolha o tipo de perfil: "

        String opcao = scanner.nextLine().trim()

        switch (opcao) {
            case "1":
                executarFluxoCandidato()
                break
            case "2":
                executarFluxoEmpresa()
                break
            case "0":
                break
            default:
                println "Opção inválida."
        }
    }

    private void executarFluxoCandidato() {
        Map dados = coletarDadosCandidato()
        if (dados.valido) {
            try {
                int id = controller.cadastrarCandidato(dados)
                if (id > 0) {
                    println "\n[SUCESSO]: Candidato cadastrado! ID: $id"
                } else {
                    println "\n[ERRO]: O banco de dados recusou o registro."
                }
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }

    private void executarFluxoEmpresa() {
        Map dados = coletarDadosEmpresa()
        if (dados.valido) {
            try {
                controller.cadastrarEmpresa(dados)
                println "\n[SUCESSO]: Empresa cadastrada!"
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }

    private Map coletarDadosCandidato() {
        try {
            println "\nFormulário de Cadastro - Candidato:"
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
        } catch (Exception e) {
            e.printStackTrace()
            return [valido: false]
        }
    }

    private Map coletarDadosEmpresa() {
        try {
            println "\nFormulário de Cadastro - Empresa:"
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
        } catch (Exception e) {
            e.printStackTrace()
            return [valido: false]
        }
    }
}