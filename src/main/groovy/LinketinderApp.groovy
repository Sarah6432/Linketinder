package main

import groovy.sql.Sql
import config.Conexao
import config.ServiceFactory
import controller.CandidatoController
import controller.EmpresaController
import controller.CadastroController
import controller.LoginController
import service.CandidatoService
import service.EmpresaService
import view.CandidatoView
import view.EmpresaView
import view.LoginView
import model.Candidato
import model.Empresa
import java.util.Scanner

class LinketinderApp {
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in)
        Sql sql = Conexao.getConn()

        CandidatoService candService = ServiceFactory.createCandidatoService(sql)
        EmpresaService empService = ServiceFactory.createEmpresaService(sql)

        CandidatoView candView = new CandidatoView()
        EmpresaView empView = new EmpresaView()
        LoginView loginView = new LoginView()

        CandidatoController candController = new CandidatoController(candService, candView)
        EmpresaController empController = new EmpresaController(empService, empView)
        CadastroController cadastroController = new CadastroController(candService, empService)
        LoginController loginController = new LoginController(sql, loginView)

        boolean executando = true

        while (executando) {
            println "\nLinketinder"
            println "1. Login"
            println "2. Cadastro: Novo Candidato"
            println "3. Cadastro: Nova Empresa"
            println "0. Sair do Sistema"
            print "Escolha uma opção: "

            if (!scanner.hasNextLine()) {
                executando = false
                break
            }

            String opcao = scanner.nextLine().trim()

            switch (opcao) {
                case "1":
                    try {
                        Object usuarioLogado = loginController.realizarLogin()

                        if (usuarioLogado == null) {
                            println ">> Login não realizado ou credenciais incorretas."
                        } else if (usuarioLogado instanceof Candidato) {
                            candController.gerenciarPerfil(usuarioLogado)
                        } else if (usuarioLogado instanceof Empresa) {
                            empController.gerenciarPainel(usuarioLogado)
                        }
                    } catch (Exception e) {
                        println ">> Erro detectado no fluxo de login:"
                        e.printStackTrace()
                    }
                    break

                case "2":
                    cadastroController.fluxoCadastroCandidato()
                    break

                case "3":
                    cadastroController.fluxoCadastroEmpresa()
                    break

                case "0":
                    println "Encerrando sistema... Até logo!"
                    executando = false
                    break

                default:
                    if (!opcao.isEmpty()) {
                        println "Opção inválida. Tente novamente."
                    }
                    break
            }
        }

        if (sql) {
            sql.close()
        }
    }
}