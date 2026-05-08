import DAO.EmpresaDAO
import DAO.VagaDAO
import DAO.CandidatoDAO
import model.Empresa
import model.Vaga
import service.EmpresaService
import spock.lang.Specification
import spock.lang.Subject

class EmpresaServiceSpec extends Specification {

    EmpresaDAO empresaDAOMock = Mock(EmpresaDAO)
    VagaDAO vagaDAOMock = Mock(VagaDAO)
    CandidatoDAO candidatoDAOMock = Mock(CandidatoDAO)

    @Subject
    EmpresaService empresaService = new EmpresaService(empresaDAOMock, vagaDAOMock, candidatoDAOMock)

    def "Deve registrar uma empresa com sucesso"() {
        given:
        Map<String, Object> dadosEmpresa = [
                nome: "TechCorp",
                cnpj: "99888777000100",
                email: "contato@techcorp.com"
        ]

        when:
        empresaService.registrarEmpresa(dadosEmpresa)

        then:
        1 * empresaDAOMock.salvar(_ as Empresa)
    }

    def "Deve listar todas as empresas cadastradas"() {
        given:
        Empresa emp1 = Mock(Empresa)
        Empresa emp2 = Mock(Empresa)

        emp1.getNome() >> "Zup"
        emp2.getNome() >> "Google"

        empresaDAOMock.listarTodos() >> [emp1, emp2]

        when:
        List<Empresa> empresas = empresaService.obterTodas()

        then:
        empresas.size() == 2
        empresas[0].getNome() == "Zup"
        empresas[1].getNome() == "Google"
    }

    def "Deve anunciar uma nova vaga corretamente"() {
        given:
        int empresaId = 10
        String titulo = "Dev Groovy"
        String descricao = "Vaga para especialistas em testes"
        String local = "Hibrido"
        List<String> competencias = ["Groovy", "Spock"]

        when:
        empresaService.anunciarVaga(empresaId, titulo, descricao, local, competencias)

        then:
        1 * vagaDAOMock.salvar(_ as Vaga)
    }

    def "Deve atualizar o perfil da empresa"() {
        given:
        Empresa empMock = Mock(Empresa)
        Map<String, String> novosDados = [
                nome: "TechCorp Atualizada",
                email: "novo@techcorp.com"
        ]

        when:
        empresaService.atualizarPerfilCompleto(empMock, novosDados)

        then:
        1 * empresaDAOMock.atualizar(empMock)
    }

    def "Deve excluir uma empresa do sistema"() {
        given:
        int idParaExcluir = 20

        when:
        empresaService.excluirEmpresa(idParaExcluir)

        then:
        1 * empresaDAOMock.deletar(idParaExcluir)
    }
}