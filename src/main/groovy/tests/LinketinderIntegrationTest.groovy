package tests

import config.Conexao
import model.CandidatoDAO
import model.EmpresaDAO
import model.VagaDAO
import model.Empresa
import model.Vaga
import org.junit.Test
import org.junit.BeforeClass
import org.junit.AfterClass
import service.CandidatoService
import service.EmpresaService
import view.CandidatoView
import view.EmpresaView
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

        CandidatoView candView = new CandidatoView()
        EmpresaView empView = new EmpresaView()

        candidatoService = new CandidatoService(candidatoDAO, candidatoDAO, candidatoDAO, vagaDAO)
        empresaService = new EmpresaService(empresaDAO, vagaDAO, candidatoDAO, empView)
    }

    @Test
    void shouldRegisterAndRetrieveCompany() {
        long ts = System.currentTimeMillis()
        String cnpj = String.valueOf(ts).take(14).toString()
        String email = "corp${ts}@test.com".toString()

        Map dados = [
                nome: "Test Corp",
                email: email,
                cnpj: cnpj,
                senha: "123",
                pais: "Brazil",
                desc: "Integration Test",
                cep: "00000"
        ]

        empresaService.registrarEmpresa(dados)

        List<Empresa> empresas = empresaService.obterTodas()
        Empresa emp = empresas.find { it.cnpj == cnpj }

        assertNotNull(emp)
        idsEmpresas.add(emp.id)
        assertEquals("Test Corp", emp.nome)
    }

    @Test
    void shouldCreateVagaSuccessfully() {
        long ts = System.currentTimeMillis()
        Map dados = [
                nome: "Vaga Test",
                email: "vaga${ts}@test.com".toString(),
                cnpj: "999",
                senha: "1",
                pais: "BR",
                desc: "D",
                cep: "1"
        ]
        empresaService.registrarEmpresa(dados)
        List<Empresa> empresas = empresaService.obterTodas()
        Empresa emp = empresas.find { it.nome == "Vaga Test" }
        idsEmpresas.add(emp.id)

        empresaService.anunciarVaga(emp.id, "Backend Dev", "Java Dev", "Remote", [])

        List<Vaga> todasVagas = vagaDAO.listarTodos()
        Vaga vaga = todasVagas.find { it.empresa?.id == emp.id && it.nome == "Backend Dev" }

        assertNotNull("A vaga deveria existir para esta empresa", vaga)
    }

    @Test
    void shouldVerifyMatchStructure() {
        try {
            List<Map> matches = empresaDAO.buscarMatchesPorEmpresa(1)
            assertNotNull(matches)
        } catch (Exception e) {
            fail("Database query failed: " + e.message)
        }
    }

    @AfterClass
    static void tearDown() {
        idsCandidatos.each { Integer id ->
            try { candidatoDAO.deletar(id) } catch(Exception e) {}
        }
        idsEmpresas.each { Integer id ->
            try { empresaDAO.deletar(id) } catch(Exception e) {}
        }
    }
}