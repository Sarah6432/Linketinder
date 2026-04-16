package config

import DAO.CandidatoDAO
import DAO.EmpresaDAO
import DAO.VagaDAO
import groovy.sql.Sql
import service.CandidatoService
import service.EmpresaService

class ServiceFactory {
    static CandidatoService createCandidatoService(Sql sql) {
        CandidatoDAO candidatoDAO = new CandidatoDAO(sql)
        VagaDAO vagaDAO = new VagaDAO(sql)
        return new CandidatoService(candidatoDAO, vagaDAO)
    }

    static EmpresaService createEmpresaService(Sql sql) {
        EmpresaDAO empresaDAO = new EmpresaDAO(sql)
        VagaDAO vagaDAO = new VagaDAO(sql)
        CandidatoDAO candidatoDAO = new CandidatoDAO(sql)
        return new EmpresaService(empresaDAO, vagaDAO, candidatoDAO)
    }
}