package model

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