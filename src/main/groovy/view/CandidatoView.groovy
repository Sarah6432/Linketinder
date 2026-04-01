package view

import model.Candidato

class CandidatoView {
    private Scanner scanner = new Scanner(System.in)

    int exibirMenuPerfil(String nomeCandidato){
        try{
        println "Bem vindo ao perfil do Candidato: "
        println "1. Visualizar Meus Dados (Listar)"
        println "2. Editar Informações (Atualizar)"
        println "3. Encerrar Minha Conta (Excluir)"
        println "4. Buscar Vagas e Curtir"
        println "0. Sair do Perfil"
        print "O que deseja fazer? "

        return Integer.parseInt(scanner.nextLine())
        }catch (NumberFormatException | InputMismatchException e) {
            throw new Exception("Erro ao receber parâmetro: ${e.message}")
        }
    }

    void exibirDadosAtuais(Candidato candidato){
        println "\nSeus dados atuais: "
        println "ID: ${candidato.id}"
        println "Nome Completo: ${candidato.nome} ${candidato.sobrenome}"
        println "E-mail: ${candidato.email}"
        println "País/CEP: ${candidato.pais} - ${candidato.cep}"
        println "Bio: ${candidato.descricao}"
        println "Competências: ${candidato.competencias?.join(', ')?: 'Nenhuma registrada'}"
    }


    Map coletarDadosParaAtualizacao(Candidato candidato) {
        println "\nEditar Perfil:"
        println "Para manter o valor atual, apenas aperte ENTER"

        try {
            print "Novo nome (${candidato.nome}): "
            String nomeInput = scanner.nextLine()

            print "Novo sobrenome (${candidato.sobrenome}): "
            String sobrenomeInput = scanner.nextLine()

            print "Novo Email (${candidato.email}): "
            String emailInput = scanner.nextLine()

            print "Nova Bio (${candidato.descricao}): "
            String descricaoInput = scanner.nextLine()

            println "Competências atuais: ${candidato.competencias?.join(', ') ?: 'Nenhuma'}"
            print "Novas competências (separe por vírgula): "
            String skillsRaw = scanner.nextLine()

            return [
                    nome: nomeInput.isEmpty() ? candidato.nome : nomeInput,
                    sobrenome: sobrenomeInput.isEmpty() ? candidato.sobrenome : sobrenomeInput,
                    email: emailInput.isEmpty() ? candidato.email : emailInput,
                    descricao: descricaoInput.isEmpty() ? candidato.descricao : descricaoInput,
                    competencias: skillsRaw.isEmpty() ? candidato.competencias?.join(',') : skillsRaw
            ]
        } catch (Exception e) {
            throw new Exception("Erro ao coletar dados: ${e.message}")
        }
    }

    boolean confirmarExclusao(){
        println "Alerta! - Seu Perfil será excluído permanentemente"
        println "Tem certeza que deseja continuar? (S/N)"
        String confirma = scanner.nextLine().trim().toUpperCase()
        return confirma == "S"
    }

    int pedirIdVagaParaCurtir() {
        print "\nDigite o ID da vaga que deseja curtir (0 para voltar): "
        try {
            return Integer.parseInt(scanner.nextLine())
        } catch (Exception e) {
            return -1
        }
    }

    void exibirVagasParaCandidato(List vagas) {
        println "\nVagas Disponíveis: "
        vagas.each { v ->
            println "ID: ${v.id} | [${v.nome}] em ${v.localEstadoCidade}"
            println "Descrição: ${v.descricao}"
            println "Requisitos: ${v.competencias?.join(', ')}"
            println "-" * 20
        }
    }

    String pedirNomeNovaCompetencia() {
        print "Digite o nome da nova competência/skill: "
        return scanner.nextLine().trim()
    }

    void exibirMinhasCompetencias(List skills) {
        println "\nSua lista de competências atual: ${skills.join(', ')}"
    }

    String pedirNomeCompetenciaParaRemover() {
        print "Digite o nome exato da competência que deseja remover: "
        return scanner.nextLine().trim()
    }


    void mostrarMensagem(String msg) {
        println "- [SISTEMA]: $msg"
    }
}
