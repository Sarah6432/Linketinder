import DAO.CandidatoDAO
import DAO.VagaDAO
import model.Candidato
import model.Vaga
import service.CandidatoService
import spock.lang.Specification
import spock.lang.Subject

class CandidatoServiceSpec extends Specification {

    CandidatoDAO candidatoDAOMock = Mock(CandidatoDAO)
    VagaDAO vagaDAOMock = Mock(VagaDAO)

    @Subject
    CandidatoService candidatoService = new CandidatoService(candidatoDAOMock, vagaDAOMock)

    def "Deve cadastrar um candidato com sucesso e retornar o ID gerado"() {
        given:
        Map<String, Object> dadosJose = [
                nome: "Jose",
                cpf: "12345678901",
                skills: ["Java", "Groovy"]
        ]

        when:
        int idResult = candidatoService.registrarNovoCandidato(dadosJose)

        then:
        1 * candidatoDAOMock.salvar(_ as Candidato) >> 101
        idResult == 101
    }

    def "Deve listar todos os candidatos cadastrados usando objetos mockados"() {
        given:
        Candidato cand1 = Mock(Candidato)
        Candidato cand2 = Mock(Candidato)

        cand1.getNome() >> "Ana"
        cand2.getNome() >> "Bruno"

        candidatoDAOMock.listarTodos() >> [cand1, cand2]

        when:
        List<Candidato> candidatos = candidatoService.obterTodos()

        then:
        candidatos.size() == 2
        candidatos[0].nome == "Ana"
        candidatos[1].nome == "Bruno"
    }

    def "Deve listar todas as vagas disponiveis"() {
        given:
        Vaga vaga1 = Mock(Vaga)
        vaga1.getNome() >> "Dev Java"

        vagaDAOMock.listarTodos() >> [vaga1]

        when:
        List<Vaga> vagas = candidatoService.listarVagasDisponiveis()

        then:
        vagas.size() == 1
        vagas[0].nome == "Dev Java"
    }

    def "Deve atualizar o perfil completo do candidato"() {
        given:
        Candidato candMock = Mock(Candidato)
        LinkedHashMap<String, String> novosDados = [
                nome: "Jose Alterado",
                competencias: "Java, Spock"
        ]

        when:
        candidatoService.atualizarPerfilCompleto(candMock, novosDados)

        then:
        1 * candidatoDAOMock.atualizar(candMock)
        1 * candidatoDAOMock.atualizarCompetenciasNoBanco(_, _)
    }

    def "Deve excluir um candidato corretamente"() {
        given:
        def idParaExcluir = 50

        when:
        candidatoService.excluirCandidato(idParaExcluir)

        then:
        1 * candidatoDAOMock.deletar(idParaExcluir)
    }
}