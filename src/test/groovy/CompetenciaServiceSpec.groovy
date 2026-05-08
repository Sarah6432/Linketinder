import DAO.CompetenciaDAO
import model.ICompetenciaManager
import service.CompetenciaService
import spock.lang.Specification
import spock.lang.Subject

class CompetenciaServiceSpec extends Specification {

    CompetenciaDAO competenciaDAOMock = Mock(CompetenciaDAO)
    ICompetenciaManager compManagerMock = Mock(ICompetenciaManager)

    @Subject
    CompetenciaService competenciaService = new CompetenciaService(competenciaDAOMock, compManagerMock)

    def "Deve vincular uma skill ao candidato com sucesso através do manager"() {
        given:
        int candidatoId = 1
        String nomeSkill = "Java"

        when:
        competenciaService.vincularAoCandidato(candidatoId, nomeSkill)

        then:
        1 * compManagerMock.vincularCompetencia(candidatoId, nomeSkill)
    }

    def "Deve listar todas as competencias chamando o DAO"() {
        given:
        List competenciasMock = [
                [id: 1, nome: "Groovy"],
                [id: 2, nome: "SQL"]
        ]

        and:
        competenciaDAOMock.listar() >> competenciasMock

        when:
        competenciaService.listarTodas()

        then:
        1 * competenciaDAOMock.listar()
    }

    def "Deve salvar uma nova competencia"() {
        given:
        String nomeComp = "Spock Framework"

        when:
        competenciaService.salvarCompetencia(nomeComp)

        then:
        1 * competenciaDAOMock.salvar(nomeComp)
    }

    def "Deve atualizar o nome de uma competencia existente"() {
        given:
        int idComp = 10
        String novoNome = "Java 17"

        when:
        competenciaService.atualizarCompetencia(idComp, novoNome)

        then:
        1 * competenciaDAOMock.atualizar(idComp, novoNome)
    }

    def "Deve excluir uma competencia pelo ID"() {
        given:
        int idParaExcluir = 5

        when:
        competenciaService.excluirCompetencia(idParaExcluir)

        then:
        1 * competenciaDAOMock.deletar(idParaExcluir)
    }

    def "Deve tratar erro quando o manager falhar ao vincular"() {
        given:
        int candidatoId = 1
        String nomeSkill = "Erro"

        and:
        compManagerMock.vincularCompetencia(candidatoId, nomeSkill) >> { throw new Exception("Falha no banco") }

        when:
        competenciaService.vincularAoCandidato(candidatoId, nomeSkill)

        then:
        notThrown(Exception)
    }
}