interface IPessoa {
    void exibirPerfil()
}

abstract class Pessoa implements IPessoa {
    String nome
    String email
    String estado
    String cep
    String descricao
    List<String> competencias = []

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
    String cpf
    int idade

    Candidato(String nome, String email, String estado, String cep, String descricao, String cpf, int idade, List<String> competencias) {
        super(nome, email, estado, cep, descricao, competencias)
        this.cpf = cpf
        this.idade = idade
    }

    @Override
    void exibirPerfil() {
        println "Candidato: $nome ($idade anos)"
        println "Email: $email | CPF: $cpf"
        println "Local: $estado - CEP: $cep"
        println "Bio: $descricao"
        println "Skills: ${competencias.join(', ')}\n"
    }
}

class Empresa extends Pessoa {
    String cnpj
    String pais

    Empresa(String nome, String email, String estado, String cep, String descricao, String cnpj, String pais, List<String> competencias) {
        super(nome, email, estado, cep, descricao, competencias)
        this.cnpj = cnpj
        this.pais = pais
    }

    @Override
    void exibirPerfil() {
        println "Empresa: $nome"
        println "Email: $email | CNPJ: $cnpj"
        println "Local: $pais ($estado) - CEP: $cep"
        println "Sobre: $descricao"
        println "Requisitos: ${competencias.join(', ')}\n"
    }
}