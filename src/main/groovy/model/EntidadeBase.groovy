package model

abstract class EntidadeBase {
    int id
    String nome, email, cep, descricao, senha, pais
    List<String> competencias = []

    EntidadeBase() {}

    EntidadeBase(String nome, String email, String cep, String pais, String descricao) {
        this.nome = nome
        this.email = email
        this.cep = cep
        this.pais = pais
        this.descricao = descricao
    }

    abstract void exibirPerfil()
}