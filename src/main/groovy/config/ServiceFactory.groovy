package config

import groovy.sql.Sql
import model.CandidatoDAO
import model.CompetenciaDAO
import model.EmpresaDAO
import model.VagaDAO
import service.CandidatoService
import service.CompetenciaService
import service.EmpresaService

class ServiceFactory {

    static CandidatoService createCandidatoService(Sql sql) {
        CandidatoDAO dao = new CandidatoDAO(sql)
        VagaDAO vDao = new VagaDAO(sql)
        return new CandidatoService(dao, dao, dao, vDao)
    }

    static EmpresaService createEmpresaService(Sql sql) {
        EmpresaDAO eDAO = new EmpresaDAO(sql)
        VagaDAO vDAO = new VagaDAO(sql)
        return new EmpresaService(eDAO, vDAO)
    }

    static CompetenciaService createCompetenciaService(Sql sql) {
        CompetenciaDAO compDAO = new CompetenciaDAO(sql)
        CandidatoDAO cDAO = new CandidatoDAO(sql)
        return new CompetenciaService(compDAO, cDAO)
    }
}