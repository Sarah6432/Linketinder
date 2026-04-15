package controller

import service.CandidatoService
import service.EmpresaService
import view.CadastroView

class CadastroController {
    private CadastroView view
    private CandidatoService candService
    private EmpresaService empService

    CadastroController(CandidatoService cs, EmpresaService es) {
        this.view = new CadastroView()
        this.candService = cs
        this.empService = es
    }

    void fluxoCadastroCandidato() {
        Map dados = view.coletarDadosCandidato()

        if (dados && dados.valido) {
            try {
                int id = candService.registrarNovoCandidato(dados)

                if (id > 0) {
                    println "\n[SUCESSO]: Candidato cadastrado! ID: $id"
                } else {
                    println "\n[ERRO]: O banco de dados recusou o registro."
                }
            } catch (Exception e) {
                println "\n[FALHA]: ${e.message}"
            }
        }
    }

    void fluxoCadastroEmpresa() {
        Map d = view.coletarDadosEmpresa()

        if (d && d.valido) {
            try {
                empService.registrarEmpresa(d)
                println "\n[SUCESSO]: Empresa cadastrada!"
            } catch (Exception e) {
                println "\n[ERRO]: ${e.message}"
            }
        }
    }
}