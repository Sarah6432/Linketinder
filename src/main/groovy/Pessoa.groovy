interface IPessoa {
    void exibirPerfil()
}

abstract class EntidadeBase implements IPessoa {
    int id
    String nome, email, cep, descricao, senha, pais
    List<String> competencias = []

    EntidadeBase(String nome, String email, String cep, String pais, String descricao) {
        this.nome = nome
        this.email = email
        this.cep = cep
        this.pais = pais
        this.descricao = descricao
    }
}

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

class Empresa extends EntidadeBase {
    String cnpj

    Empresa(String n, String ec, String c, String p, String d, String cj) {
        super(n, ec, c, p, d)
        this.cnpj = cj
    }

    @Override
    void exibirPerfil() {
        println "ID: $id | Empresa: $nome"
        println "CNPJ: $cnpj | Local: $pais | Contato: $email"
        println "Descrição: $descricao\n"
    }
}