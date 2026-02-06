class Curtida {
    Candidato candidato
    Empresa empresa
    boolean candidatoCurtiu = false
    boolean empresaCurtiu = false

    Curtida(Candidato candidato, Empresa empresa) {
        this.candidato = candidato
        this.empresa = empresa
    }

    boolean isMatch() {
        return candidatoCurtiu && empresaCurtiu
    }
}