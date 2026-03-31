package model

class Empresa {
    int id
    String nome
    String email
    String pais
    String cep
    String descricao
    String cnpj
    String senha

    Empresa(String nome, String email, String pais, String cep, String descricao, String cnpj) {
        this.nome = nome
        this.email = email
        this.pais = pais
        this.cep = cep
        this.descricao = descricao
        this.cnpj = cnpj
    }

    Empresa() {}

}