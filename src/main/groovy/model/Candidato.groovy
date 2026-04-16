package model

class Candidato extends EntidadeBase {
    String sobrenome, cpf, dataNascimento



    Candidato(String n, String e, String p, String c, String d, String cp, List<String> s) {
        super(n, e, c, p, d)
        this.cpf = cp
        this.competencias = s
    }

    @Override
    void exibirPerfil() {
        println "ID: $id | Candidato: $nome $sobrenome"
        println "CPF: $cpf | Nascimento: $dataNascimento"
        println "Local: $pais - ($cep)"
        println "Skills: ${competencias.join(', ')}\n"
    }
}