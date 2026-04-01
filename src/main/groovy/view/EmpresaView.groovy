package view

import model.Empresa
import model.Vaga

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

    Map coletarEdicaoVaga(Vaga vaga) {
        println "\nEditando Vaga: ${vaga.nome}"
        println "Deixe em branco para manter o valor atual"

        print "Novo Título [${vaga.nome}]: "
        String nome = scanner.nextLine()

        print "Nova Descrição [${vaga.descricao}]: "
        String descricao = scanner.nextLine()

        println "Requisitos atuais: ${vaga.competencias?.join(', ') ?: 'Nenhum'}"
        print "Novos Requisitos (separe por vírgula): "
        String requisitosRaw = scanner.nextLine()

        return [
                nome: nome.isEmpty() ? vaga.nome : nome,
                descricao: descricao.isEmpty() ? vaga.descricao : descricao,
                requisitos: requisitosRaw.isEmpty() ? vaga.competencias?.join(',') : requisitosRaw
        ]
    }

    void exibirDadosEmpresa(Empresa emp){
      println "Dados da Empresa: "
      println "Nome: ${emp.nome}"
      println "CNPJ: ${emp.cnpj}"
      println "E-mail: ${emp.email}"
      println "Cep: ${emp.cep}"
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

    void exibirPerfilDetalhado(Map c) {
        if (!c) {
            println "candidato nao encontrado."
            return
        }
        println "\nperfil do candidato:"
        println "nome: ${c.nome} ${c.sobrenome ?: ''}"
        println "email: ${c.email}"
        println "cep: ${c.cep ?: 'nao informado'}"
        println "competencias: ${c.lista_competencias ?: 'nenhuma registrada'}"
        println "---------------------------\n"
    }

    int pedirId(String acao) {
        print "Digite o ID para $acao: "
        try {
            return Integer.parseInt(scanner.nextLine())
        } catch (Exception e) {
            return -1
        }
    }

    void mostrarMensagem(String msg) {
        println "- [SISTEMA]: $msg"
    }

    String lerEntrada(String mensagem) {
        print mensagem
        return scanner.nextLine()
    }
}
