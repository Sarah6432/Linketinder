package controller

import model.Candidato
import model.Empresa
import service.EmpresaService

class EmpresaController {
    private final EmpresaService service

    EmpresaController(EmpresaService service) {
        this.service = service
    }

    void atualizarPerfil(Empresa empresa, Map dados) {
        service.atualizarPerfilCompleto(empresa, dados)
    }

    void anunciarVaga(int empresaId, String titulo, String descricao, String local, List requisitos) {
        service.anunciarVaga(empresaId, titulo, descricao, local, requisitos)
    }

    List listarVagasPorEmpresa(int empresaId) {
        return service.listarVagasPorEmpresa(empresaId) ?: []
    }

    Object buscarVagaPorId(int idVaga) {
        return service.buscarVagaPorId(idVaga)
    }

    void atualizarVaga(int idVaga, Map dados) {
        service.atualizarVagaCompleta(idVaga, dados)
    }

    boolean verificarPropriedadeVaga(int vagaId, int empresaId) {
        return service.vagaPertenceAEmpresa(vagaId, empresaId)
    }

    void excluirVaga(int idVaga) {
        service.excluirVaga(idVaga)
    }

    List<Map> listarInteressados(int empresaId) {
        return service.listarCandidatosQueCurtiuEmpresa(empresaId) ?: []
    }

    Map gerenciarInteresse(int empId, int candId) {
        return service.gerenciarInteresse(empId, candId)
    }

    List<Candidato> obterMatchesCompletos(int empresaId) {
        return service.obterMatchesCompletos(empresaId) ?: []
    }

    void excluirContaEmpresa(int empresaId) {
        service.excluirEmpresa(empresaId)
    }
}