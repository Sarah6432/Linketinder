package model

class Empresa extends EntidadeBase {
    String cnpj

    Empresa() {
        super()
    }

    Empresa(String nome, String email, String pais, String cep, String descricao, String cnpj) {
        super(nome, email, cep, pais, descricao)
        this.cnpj = cnpj
    }

    @Override
    void exibirPerfil() {
        println "Perfil da Empresa:"
        println "ID: $id | Nome: $nome"
        println "CNPJ: $cnpj | Email: $email"
        println "Local: $pais - CEP: $cep"
        println "Descrição: $descricao"
    }
}