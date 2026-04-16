package controller

import DAO.CandidatoDAO
import DAO.EmpresaDAO
import groovy.sql.Sql
import model.Candidato
import model.Empresa
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Pack

class LoginController{
    private CandidatoDAO candidatoDAO
    private EmpresaDAO empresaDAO

    LoginController(Sql sql){
        this.candidatoDAO = new CandidatoDAO(sql)
        this.empresaDAO = new EmpresaDAO(sql)
    }

    Object autenticar(String email, String senha){
         try {
             Candidato candidato = candidatoDAO.buscarPorEmail(email)
             if (candidato && candidato.senha == senha) return candidato

             Empresa empresa = empresaDAO.buscarPorEmail(email)
             if (empresa && empresa.senha == senha) return empresa
         }catch (Exception e){
           e.printStackTrace()
         }

    }
}