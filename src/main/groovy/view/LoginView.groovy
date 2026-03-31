package view

class LoginView {
    private Scanner scanner = new Scanner(System.in)

    Map coletarCredenciais(){
        try{
            println "Bem vindo ao Linketinder - Login do Sistema"
            println "E-mail: "
            String email = scanner.nextLine()
            println "Senha: "
            String senha = scanner.nextLine()
            return [email: email, senha: senha, valido: true]
        }catch (Exception e){
            throw new Exception("Falha na leitura de email e senha: ${e.message}")
        }
    }

    void mostrarSucesso(String mensagem) {
        println "Sucesso: $mensagem"
    }

    void mostrarErro(String mensagem) {
        println "Error: $mensagem"
    }
}
