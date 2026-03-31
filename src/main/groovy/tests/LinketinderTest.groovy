package tests

import model.Candidato
import model.Curtida
import model.Empresa
import model.Vaga
import org.junit.Test
import org.junit.Before
import static org.junit.Assert.*

class LinketinderTest {
    private List<Candidato> candidatos
    private List<Empresa> empresas
    private List<Vaga> vagas

    @Before
    void setup() {
        candidatos = []
        empresas = []
        vagas = []
    }

    @Test
    void shouldRegisterNewCandidatoCorrectly() {
        def skills = ["Java", "Groovy"]
        def candidato = new Candidato("Sarah Lima", "sarah@email.com", "Brasil", "44000-000", "Dev", "123.456.789-11", 21, skills)

        candidatos.add(candidato)

        assertEquals(1, candidatos.size())
        assertEquals("Sarah Lima", candidatos[0].nome)
        assertEquals("Brasil", candidatos[0].pais)
    }

    @Test
    void shouldRegisterNewEmpresaCorrectly() {
        def empresa = new Empresa("ZG Soluções", "rh@zg.com", "GO", "74000-000", "Fintech", "77.888.999/0001-00", "Brasil", ["Java"])

        this.empresas.add(empresa)

        assertEquals(1, empresas.size())
        assertEquals("ZG Soluções", empresas[0].nome)
        assertEquals("Brasil", empresas[0].pais)
    }

    @Test
    void shouldConfirmMatchWhenEmpresaReciprocatesInterest() {
        def candidato = new Candidato("Sarah", "s@e.com", "Brasil", "000", "Dev", "111", 21, ["Java"])
        def empresa = new Empresa("ZG", "z@e.com", "GO", "000", "Tech", "222", "Brasil", ["Java"])
        def vaga = new Vaga("Dev", "Desc", ["Java"], empresa)
        def curtida = new Curtida(candidato, vaga)

        curtida.empresaCurtiu = true

        assertTrue("Deveria ser um match quando a empresa retribui o interesse", curtida.isMatch())
    }

    @Test
    void shouldNotBeMatchBeforeEmpresaInterest() {
        def candidato = new Candidato("João Silva", "joao@email.com", "Brasil", "01000", "Dev", "123", 25, ["Python"])
        def empresa = new Empresa("Tech", "rh@tech.com", "SP", "01000", "TI", "456", "Brasil", ["Python"])
        def vaga = new Vaga("model.Vaga Python", "Desc", ["Python"], empresa)
        def curtida = new Curtida(candidato, vaga)

        assertFalse("Não deve ser match até que a empresa registre interesse no candidato", curtida.isMatch())
    }

    @Test
    void shouldCreateVagaLinkedToEmpresa() {
        def empresa = new Empresa("Tech", "rh@tech.com", "SP", "01000", "TI", "456", "Brasil", ["Java"])
        def vaga = new Vaga("Desenvolvedor", "Desc", ["Java"], empresa)

        vagas.add(vaga)

        assertEquals(1, vagas.size())
        assertNotNull("A vaga deve possuir uma empresa vinculada", vagas[0].empresa)
        assertEquals("Tech", vagas[0].empresa.nome)
    }
}