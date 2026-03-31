package view

import model.Empresa
import model.Vaga
import model.VagaDAO

class EmpresaView {
    private Scanner scanner = new Scanner(System.in)

    int exibirMenuEmpresa(String nomeEmpresa){
        try {
            println "Painel da Empresa - $nomeEmpresa"
            println "1. Ver Perfil da Empresa"
            println "2. Editar Perfil da Empresa"
            println "3. Anunciar Nova Vaga"
            println "4. Listar Minhas Vagas"
            println "5. Editar Vaga Existente"
            println "6. Excluir Vaga"
            println "7. Ver Candidatos Interessados / Matches"
            println "8. Encerrar conta da Empresa"
            println "0. Sair / Logout"
            println "Escolha uma opção: "

            return Integer.parseInt(scanner.nextLine())
        }catch (Exception e) {
           throw new Exception("Erro receber escolha do menu da empresa: ${e.message}")
        }
    }

    Map coletarDadosNovaVaga(){
        println "Anunciar nova vaga: "
        try{
            println "Titulo da Vaga: "
            String t = scanner.nextLine()
            println "Descrição: "
            String d = scanner.nextLine()
            println "Local (Cidade/Estado): "
            String l = scanner.nextLine()
            println "Competências necessárias (separe por vírgula): "
            String r = scanner.nextLine()

            return [titulo: t, descricao: d, local: l, requisitosRaw: r, valido: true]
        }catch(Exception e){
            throw new Exception("Erro ao anunciar vaga: ${e.message}")
        }
    }

    Map coletarEdicaoVaga(Vaga vaga){
       println "Editar vaga (id: ${vaga.id})"
       println "(Deixe vazio para manter o valor atual)"
       println "Novo Título (${vaga.nome})"
       String t = scanner.nextLine()
       println "Nova descrição (${vaga.descricao}): "
       String d = scanner.nextLine()

       return [
               titulo: t.isEmpty() ? vaga.nome : t,
               descricao: d.isEmpty() ? vaga.descricao : d,
       ]
   }

    void exibirDadosEmpresa(Empresa emp){
      println "Dados da Empresa: "
      println "Nome: ${emp.nome}"
      println "CNPJ: ${emp.cnpj}"
      println "CNPJ: ${emp.email}"
      println "CNPJ: ${emp.cep}"
      println "Descrição: ${emp.descricao}"
    }

    Map coletarEdicaoPerfil(Empresa emp) {
        println "Editar perfil corporativo: "
        println "Novo Nome (${emp.nome}): "
        String n = scanner.nextLine()
        println "Novo Email (${emp.email}): "
        String e = scanner.nextLine()
        println "Nova Descrição (${emp.descricao}): "
        String d = scanner.nextLine()

        return [
                nome: n.isEmpty() ? emp.nome : n,
                email: e.isEmpty() ? emp.email : e,
                descricao: d.isEmpty() ? emp.descricao : d
        ]
    }

    boolean confirmarExclusaoEmpresa(){
        println "Atenção! Está ação não pode ser desfeita..."
        println "Ao excluir a empresa, todas as suas vagas"
        println "e histórico de curtidas serão apagados."
        print "Tem certeza que deseja excluir a empresa? (S/N): "

        String resposta = scanner.nextLine().trim().toUpperCase()
        return resposta == "S"
    }

    int pedirId(String acao) {
        print "Digite o ID da vaga para $acao: "
        try {
            return Integer.parseInt(scanner.nextLine())
        } catch (Exception e) {
            return -1
        }
    }

    void mostrarMensagem(String msg) {
        println ">> [SISTEMA]: $msg"
    }

}
