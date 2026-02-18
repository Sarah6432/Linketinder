interface IPessoa {
    void exibirPerfil()
}

abstract class Pessoa implements IPessoa {
    String nome, email, estado, cep, descricao
    List<String> competencias = []

    Pessoa(String nome, String email, String estado, String cep, String descricao, List<String> competencias) {
        this.nome = nome; this.email = email; this.estado = estado
        this.cep = cep; this.descricao = descricao; this.competencias = competencias
    }
}

class Candidato extends Pessoa {
    String cpf
    int idade

    Candidato(String n, String e, String st, String c, String d, String cp, int id, List<String> s) {
        super(n, e, st, c, d, s)
        this.cpf = cp
        this.idade = id
    }

    @Override
    void exibirPerfil() {
        println "Candidato: $nome | CPF: $cpf | Idade: $id"
        println "Local: $estado ($cep) | Contato: $email"
        println "Bio: $descricao | Skills: ${competencias.join(', ')}\n"
    }
}

class Empresa extends Pessoa {
    String cnpj, pais

    Empresa(String n, String ec, String st, String c, String d, String cj, String p, List<String> r) {
        super(n, ec, st, c, d, r)
        this.cnpj = cj
        this.pais = p
    }

    @Override
    void exibirPerfil() {
        println "Empresa: $nome | CNPJ: $cnpj | País: $pais"
        println "Email Corporativo: $email | Local: $estado"
        println "Descrição: $descricao | Requisitos: ${competencias.join(', ')}\n"
    }
}