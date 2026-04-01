package config

import groovy.sql.Sql
import model.CandidatoDAO
import model.CompetenciaDAO
import model.EmpresaDAO
import model.VagaDAO
import service.CandidatoService
import service.CompetenciaService
import service.EmpresaService
import view.EmpresaView

class ServiceFactory {

    static CandidatoService createCandidatoService(Sql sql) {
        CandidatoDAO dao = new CandidatoDAO(sql)
        VagaDAO vDao = new VagaDAO(sql)
        return new CandidatoService(dao, dao, dao, vDao)
    }

    static EmpresaService createEmpresaService(Sql sql) {
        EmpresaDAO eDAO = new EmpresaDAO(sql)
        VagaDAO vDAO = new VagaDAO(sql)
        CandidatoDAO cDAO = new CandidatoDAO(sql)
        EmpresaView eView = new EmpresaView()

        return new EmpresaService(eDAO, vDAO, cDAO, eView)
    }
}