import org.junit.Test
import org.junit.BeforeClass
import org.junit.AfterClass
import static org.junit.Assert.*
import groovy.sql.Sql

class LinketinderIntegrationTest {
    private static Sql db
    private static CandidatoDAO candidatoDAO
    private static EmpresaDAO empresaDAO
    private static VagaDAO vagaDAO
    private static CandidatoService candidatoService
    private static EmpresaService empresaService
    private static List<Integer> idsCandidatos = []
    private static List<Integer> idsEmpresas = []

    @BeforeClass
    static void setup() {
        db = Conexao.getConn()
        candidatoDAO = new CandidatoDAO(db)
        empresaDAO = new EmpresaDAO(db)
        vagaDAO = new VagaDAO(db)

        candidatoService = new CandidatoService(candidatoDAO, candidatoDAO, candidatoDAO)
        empresaService = new EmpresaService(empresaDAO, vagaDAO)
    }

    @Test
    void shouldRegisterAndRetrieveCandidato() {
        long ts = System.currentTimeMillis()
        String email = "test${ts}@solid.com"
        String cpf = String.valueOf(ts).take(11)

        candidatoService.registrarNovoCandidato(
                "Test", "Solid", "2000-01-01", email, cpf, "Brasil", "00000", "Bio", "123", ["Groovy"]
        )

        def consultado = candidatoService.obterTodos().find { it.email == email }
        assertNotNull(consultado)
        idsCandidatos.add(consultado.id)
        assertEquals("Test", consultado.nome)
    }

    @Test
    void shouldRegisterEmpresaAndCreateVaga() {
        long ts = System.currentTimeMillis()
        String cnpj = String.valueOf(ts).take(14)
        String email = "corp${ts}@solid.com"

        empresaService.registrarEmpresa("Tech SA", email, cnpj, "111", "Brasil", "Desc", "123")
        def emp = empresaService.obterTodas().find { it.cnpj == cnpj }
        assertNotNull(emp)
        idsEmpresas.add(emp.id)

        empresaService.anunciarVaga(emp.id, "Dev Java", "Desc Vaga", "Remoto", ["Java", "SQL"])

        def todasVagas = vagaDAO.listarTodos()
        def vagaExistente = todasVagas.any { it.nome == "Dev Java" && it.empresa.id == emp.id }

        assertTrue(vagaExistente)
    }

    @Test
    void shouldHandleVagaDeletion() {
        long ts = System.currentTimeMillis()
        empresaService.registrarEmpresa("Del SA", "del${ts}@test.com", "000", "000", "Brasil", "B", "1")
        def emp = empresaService.obterTodas().find { it.nome == "Del SA" }
        idsEmpresas.add(emp.id)

        empresaService.anunciarVaga(emp.id, "Temp", "D", "L", ["X"])
        def vaga = vagaDAO.listarTodos().find { it.empresa.id == emp.id }

        empresaService.excluirVaga(vaga.id)
        def vagaPosDel = vagaDAO.listarTodos().find { it.id == vaga.id }
        assertNull(vagaPosDel)
    }

    @AfterClass
    static void tearDown() {
        idsCandidatos.each { try { candidatoDAO.deletar(it) } catch(e) {} }
        idsEmpresas.each { try { empresaDAO.deletar(it) } catch(e) {} }
    }
}