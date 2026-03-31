package controller

import groovy.sql.Sql
import model.Candidato
import model.Empresa
import model.CandidatoDAO
import model.EmpresaDAO
import view.LoginView

class LoginController {
    private Sql sql
    private LoginView view
    private CandidatoDAO candidatoDAO
    private EmpresaDAO empresaDAO

    LoginController(Sql sql, LoginView view) {
        this.sql = sql
        this.view = view
        this.candidatoDAO = new CandidatoDAO(sql)
        this.empresaDAO = new EmpresaDAO(sql)
    }

    Object realizarLogin() {
        Map credenciais = view.coletarCredenciais()

        if (credenciais.valido) {
            String email = credenciais.email

            Candidato cand = candidatoDAO.buscarPorEmail(email)
            if (cand) {
                view.mostrarSucesso("Candidato ${cand.nome} logado!")
                return cand
            }

            Empresa emp = empresaDAO.buscarPorEmail(email)
            if (emp) {
                view.mostrarSucesso("Empresa ${emp.nome} logada!")
                return emp
            }

            view.mostrarErro("E-mail ou senha inválidos.")
        }
        return null
    }
}