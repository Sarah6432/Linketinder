package controller

import service.CandidatoService
import service.EmpresaService

class CadastroController {
    private CandidatoService candService
    private EmpresaService empService

    CadastroController(CandidatoService candidato, EmpresaService empresa) {
        this.candService = candidato
        this.empService = empresa
    }

    int cadastrarCandidato(Map dados) {
        try {
            candService.registrarNovoCandidato(dados)
        } catch (Exception e) {
            e.printStackTrace()
            return 0
        }
    }

    void cadastrarEmpresa(Map dados) {
        try {
            empService.registrarEmpresa(dados)
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}