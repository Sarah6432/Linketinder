import config.Conexao
import config.ServiceFactory
import controller.CadastroController
import controller.CandidatoController
import controller.EmpresaController
import controller.LoginController
import groovy.sql.Sql
import model.Candidato
import model.Empresa
import service.CandidatoService
import service.EmpresaService
import view.CadastroView
import view.CandidatoView
import view.EmpresaView
import view.LoginView
import java.util.Scanner

class LinketinderApp {
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in)
        Sql sql = Conexao.getConn()

        CandidatoService candService = ServiceFactory.createCandidatoService(sql)
        EmpresaService empService = ServiceFactory.createEmpresaService(sql)

        CandidatoController candController = new CandidatoController(candService)
        EmpresaController empController = new EmpresaController(empService)
        CadastroController cadastroController = new CadastroController(candService, empService)
        LoginController loginController = new LoginController(sql)

        LoginView loginView = new LoginView(loginController)
        CandidatoView candView = new CandidatoView(candController)
        EmpresaView empView = new EmpresaView(empController)
        CadastroView cadastroView = new CadastroView(cadastroController)

        boolean executando = true

        while (executando) {
            println "\nLinketinder: "
            println "1. Login"
            println "2. Cadastro (Candidato ou Empresa)"
            println "0. Sair"
            print "Escolha: "

            String opcao = scanner.nextLine().trim()

            switch (opcao) {
                case "1":
                    Object logado = loginView.renderizarLogin()

                    if (logado instanceof Candidato) {
                        candView.gerenciarPerfil((Candidato) logado)
                    } else if (logado instanceof Empresa) {
                        empView.gerenciarPainel((Empresa) logado)
                    }
                    break

                case "2":
                    cadastroView.exibirMenuCadastro()
                    break

                case "0":
                    println "Encerrando sistema..."
                    executando = false
                    break

                default:
                    if (!opcao.isEmpty()) {
                        println "Opção inválida."
                    }
                    break
            }
        }

        if (sql != null) {
            sql.close()
        }
    }
}