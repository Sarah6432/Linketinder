package view

import controller.LoginController

class LoginView {
    private Scanner scanner = new Scanner(System.in)
    private LoginController controller

    LoginView(LoginController controller){
        this.controller = controller
    }

    Object renderizarLogin(){
        try {
            println "Login - Linketinder: "
            print "E-mail: "
            String email = scanner.nextLine()
            print "Senha: "
            String senha = scanner.nextLine()

            Object usuarioLogado = controller.autenticar(email, senha)

            if (usuarioLogado) {
                println "Sucesso: Bem-vindo! "
                return usuarioLogado
            } else {
                println "Erro: Credenciais inválidas."
                return null
            }
        }catch (Exception e){
            e.printStackTrace()
            return null
        }
    }
}