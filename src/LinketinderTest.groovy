import org.junit.Test
import org.junit.Before
import static org.junit.Assert.*

class LinketinderTest {
    List<Candidato> candidatos
    List<Empresa> empresas
    List<Vaga> vagas

    @Before
    void setup() {
        candidatos = []
        empresas = []
        vagas = []
    }

    @Test
    void InserirCandidatoNovo() {
        def novoCandidato = new Candidato("Sarah Lima", "sarah@email.com", "BA", "44000-000", "Dev", "123.456.789-11", 21, ["Java", "Groovy"])
        candidatos.add(novoCandidato)
        assertEquals(1, candidatos.size())
        assertEquals("Sarah Lima", candidatos[0].nome)
    }

    @Test
    void InserirNovaEmpresa() {
        def novaEmpresa = new Empresa("ZG Soluções", "rh@zg.com", "GO", "74000-000", "Fintech", "77.888.999/0001-00", "Brasil", ["Java"])
        empresas.add(novaEmpresa)
        assertEquals(1, empresas.size())
        assertEquals("ZG Soluções", empresas[0].nome)
    }

    @Test
    void MatchFuncional() {
        def c = new Candidato("Sarah", "s@e.com", "BA", "000", "Dev", "111", 21, ["Java"])
        def e = new Empresa("ZG", "z@e.com", "GO", "000", "Tech", "222", "BR", ["Java"])
        def v = new Vaga("Dev", "Desc", ["Java"], e)
        def curtida = new Curtida(c, v)

        curtida.empresaCurtiu = true

        assertTrue("Deveria ser um match quando a empresa retribui", curtida.isMatch())
    }

    @Test
    void testAnonimatoAntesDoMatch() {
        def c = new Candidato("João Silva", "joao@email.com", "SP", "01000", "Dev", "123", 25, ["Python"])
        def e = new Empresa("Tech", "rh@tech.com", "SP", "01000", "TI", "456", "Brasil", ["Python"])
        def v = new Vaga("Vaga Python", "Desc", ["Python"], e)
        def curtida = new Curtida(c, v)

        assertFalse("Não deve ser match até a empresa curtir", curtida.isMatch())
    }

    @Test
    void testCriacaoDeVaga() {
        def e = new Empresa("Tech", "rh@tech.com", "SP", "01000", "TI", "456", "Brasil", ["Java"])
        def v = new Vaga("Desenvolvedor", "Desc", ["Java"], e)
        vagas.add(v)

        assertEquals(1, vagas.size())
        assertEquals("Tech", vagas[0].empresa.nome)
    }
}