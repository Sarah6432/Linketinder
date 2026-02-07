import org.junit.Test
import org.junit.Before
import static org.junit.Assert.*

class LinketinderTest {
    List<Candidato> candidatos
    List<Empresa> empresas

    @Before
    void setup() {
        candidatos = []
        empresas = []
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
        def c = new Candidato("Sarah", "s@e.com", "BA", "000", "Dev", "111", 21, [])
        def e = new Empresa("ZG", "z@e.com", "GO", "000", "Tech", "222", "BR", [])
        def curtida = new Curtida(c, e)

        curtida.candidatoCurtiu = true
        curtida.empresaCurtiu = true

        assertTrue("Deveria ser um match quando ambos curtem", curtida.isMatch())
    }
}