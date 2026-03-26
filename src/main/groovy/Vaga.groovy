class Vaga {
    int id
    String nome
    String descricao
    String localEstadoCidade
    List<String> competencias = []
    Empresa empresa

    Vaga(String nome, String descricao, List<String> competencias, Empresa empresa) {
        this.nome = nome
        this.descricao = descricao
        this.competencias = competencias ?: []
        this.empresa = empresa
    }

    void exibirVaga(int index) {
        println "------------------------------------------"
        println "[$index] ID Banco: $id | Vaga: $nome"
        println "Empresa: ${empresa.nome} | Local: $localEstadoCidade"
        println "Descrição: $descricao"
        if (competencias) {
            println "Requisitos: ${competencias.join(', ')}"
        }
    }
}