class Curtida {
    Candidato candidato
    Vaga vaga
    boolean empresaCurtiu = false

    Curtida(Candidato candidato, Vaga vaga) {
        this.candidato = candidato
        this.vaga = vaga
    }

    boolean isMatch() {
        return empresaCurtiu
    }

    void exibirParaEmpresa(int index) {
        if (isMatch()) {
            println "[$index] MATCH! Nome: ${candidato.nome} | Email: ${candidato.email} | Skills: ${candidato.competencias.join(', ')}"
        } else {
            println "[$index] [Candidato Anônimo] | Skills: ${candidato.competencias.join(', ')}"
        }
    }
}