class Vaga {
    String nome
    String descricao
    List<String> requisitos
    Empresa empresa

    Vaga(String nome, String descricao, List<String> requisitos, Empresa empresa) {
        this.nome = nome
        this.descricao = descricao
        this.requisitos = requisitos
        this.empresa = empresa
    }

    void exibirVaga(int index) {
        println "[$index] Vaga: $nome | Empresa: ${empresa.nome}"
        println "Descrição: $descricao"
        println "Requisitos: ${requisitos.join(', ')}\n"
    }
}