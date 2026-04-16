package tests

import config.Conexao
import DAO.CandidatoDAO
import DAO.EmpresaDAO
import DAO.VagaDAO
import model.Empresa
import model.Vaga
import model.Candidato
import org.junit.Test
import org.junit.BeforeClass
import org.junit.AfterClass
import service.CandidatoService
import service.EmpresaService
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

        candidatoService = new CandidatoService(candidatoDAO, vagaDAO)
        empresaService = new EmpresaService(empresaDAO, vagaDAO, candidatoDAO)
    }

    @Test
    void shouldRegisterAndRetrieveCompany() {
        long ts = System.currentTimeMillis()
        String cnpj = String.valueOf(ts).take(14)
        String email = "corp${ts}@test.com"

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
        if (emp) idsEmpresas.add(emp.id)
        assertEquals("Test Corp", emp?.nome)
    }

    @Test
    void shouldCreateVagaSuccessfully() {
        long ts = System.currentTimeMillis()
        String cnpjVaga = "V" + String.valueOf(ts).take(13)

        Map dadosEmpresa = [
                nome: "Vaga Test Corp",
                email: "vaga_emp${ts}@test.com",
                cnpj: cnpjVaga,
                senha: "1",
                pais: "BR",
                desc: "Teste de Vaga",
                cep: "12345"
        ]

        empresaService.registrarEmpresa(dadosEmpresa)
        List<Empresa> empresas = empresaService.obterTodas()
        Empresa emp = empresas.find { it.cnpj == cnpjVaga }

        assertNotNull(emp)
        idsEmpresas.add(emp.id)

        empresaService.anunciarVaga(emp.id, "Backend Dev Integration", "Java/Groovy Experience", "Remote", ["Java", "SQL"])

        List<Vaga> todasVagas = vagaDAO.listarTodos()
        Vaga vaga = todasVagas.find { it.empresa?.id == emp.id && it.nome == "Backend Dev Integration" }

        assertNotNull(vaga)
        assertFalse(vaga.competencias.isEmpty())
    }

    @Test
    void shouldRegisterCandidatoSuccessfully() {
        long ts = System.currentTimeMillis()
        String cpf = String.valueOf(ts).take(11)

        Map dadosCand = [
                nome: "Jose",
                sobrenome: "Silva",
                email: "jose${ts}@test.com",
                cpf: cpf,
                data: "1990-01-01",
                pais: "Brasil",
                cep: "11111",
                bio: "Bio teste",
                senha: "123",
                skills: ["Groovy", "JUnit"]
        ]

        int id = candidatoService.registrarNovoCandidato(dadosCand)
        assertTrue(id > 0)
        idsCandidatos.add(id)

        List<Candidato> lista = candidatoDAO.listarTodos()
        Candidato jose = lista.find { it.id == id }

        assertNotNull(jose)
        assertEquals("Jose", jose.nome)
        assertTrue(jose.competencias.contains("Groovy"))
    }

    @Test
    void shouldVerifyMatchStructure() {
        try {
            List<Map> matches = empresaDAO.listarMatchesReais(0)
            assertNotNull(matches)
        } catch (Exception e) {
            fail("Erro na query de matches: " + e.message)
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