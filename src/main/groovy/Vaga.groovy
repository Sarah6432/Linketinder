class Vaga {
    int id // Adicionado para identificar a vaga no banco (PK)
    String nome
    String descricao
    String localEstadoCidade
    List<String> requisitos = []
    Empresa empresa

    Vaga(String nome, String descricao, List<String> requisitos, Empresa empresa) {
        this.nome = nome
        this.descricao = descricao
        this.requisitos = requisitos
        this.empresa = empresa
    }

    void exibirVaga(int index) {
        println "------------------------------------------"
        println "[$index] ID Banco: $id | Vaga: $nome"
        println "Empresa: ${empresa.nome} | Local: $localEstadoCidade"
        println "Descrição: $descricao"
        if (requisitos) {
            println "Requisitos: ${requisitos.join(', ')}"
        }
    }
}