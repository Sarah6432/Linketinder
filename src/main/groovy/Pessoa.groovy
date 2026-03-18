interface IPessoa {
    void exibirPerfil()
}

abstract class Pessoa implements IPessoa {
    int id;
    String nome, email, estado, cep, descricao, senha, pais;
    List<String> competencias = [];

    Pessoa(String nome, String email, String estado, String cep, String descricao, List<String> competencias) {
        this.nome = nome
        this.email = email
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
    }
}

class Candidato extends Pessoa {
    String sobrenome, cpf, dataNascimento;
    int idade;

    Candidato(String n, String e, String st, String c, String d, String cp, int id, List<String> s) {
        super(n, e, st, c, d, s)
        this.cpf = cp
        this.idade = id
    }

    @Override
    void exibirPerfil() {
        println "------------------------------------------"
        println "ID: $id | Candidato: $nome $sobrenome"
        println "CPF: $cpf | Nascimento: $dataNascimento"
        println "Local: $pais - $estado ($cep)"
        println "Contato: $email"
        println "Bio: $descricao"
        println "Skills: ${competencias.join(', ')}"
        println "------------------------------------------"
    }
}

class Empresa extends Pessoa {
    String cnpj

    Empresa(String n, String ec, String st, String c, String d, String cj, String p, List<String> r) {
        super(n, ec, st, c, d, r)
        this.cnpj = cj
        this.pais = p
    }

    @Override
    void exibirPerfil() {
        println "------------------------------------------"
        println "ID: $id | Empresa: $nome"
        println "CNPJ: $cnpj | País: $pais"
        println "Local: $estado ($cep) | Contato: $email"
        println "Descrição: $descricao"
        println "------------------------------------------"
    }
}