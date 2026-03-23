import groovy.sql.Sql

class CandidatoDAO {
    private final Sql db

    CandidatoDAO(Sql sql) {
        this.db = sql
    }

    int salvar(Candidato cand) {
        try {
            def sql = """
                INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao_pessoal, senha) 
                VALUES (?, ?, CAST(? AS DATE), ?, ?, ?, ?, ?, ?)
            """
            def params = [
                    cand.nome, cand.sobrenome, cand.dataNascimento, cand.email,
                    cand.cpf, cand.pais, cand.cep, cand.descricao, cand.senha
            ]

            def keys = db.executeInsert(sql, params)
            return keys[0][0] as int
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar candidato: ${e.message}", e)
        }
    }

    void atualizar(Candidato cand) {
        try {
            def sql = """
                UPDATE candidatos 
                SET nome = ?, sobrenome = ?, data_nascimento = CAST(? AS DATE), 
                    email = ?, cpf = ?, pais = ?, cep = ?, descricao_pessoal = ?
                WHERE id = ?
            """
            def params = [
                    cand.nome, cand.sobrenome, cand.dataNascimento, cand.email,
                    cand.cpf, cand.pais, cand.cep, cand.descricao, cand.id
            ]

            db.executeUpdate(sql, params)
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar candidato ${cand.id}: ${e.message}", e)
        }
    }

    void deletar(int candidatoId) {
        try {
            db.execute("DELETE FROM candidatos WHERE id = ?", [candidatoId])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar candidato $candidatoId: ${e.message}", e)
        }
    }

    List<Candidato> listarTodos() {
        try {
            return db.rows("SELECT * FROM candidatos ORDER BY id ASC").collect { row ->
                mapRowToCandidato(row)
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar candidatos: ${e.message}", e)
        }
    }

    void vincularCompetencia(int candidatoId, String nomeSkill) {
        try {
            db.execute("INSERT INTO competencias (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING", [nomeSkill])

            def sqlVincular = """
                INSERT INTO candidato_competencias (candidato_id, competencia_id)
                SELECT ?, id FROM competencias WHERE nome = ?
                ON CONFLICT DO NOTHING
            """
            db.execute(sqlVincular, [candidatoId, nomeSkill])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao vincular skill $nomeSkill ao candidato $candidatoId", e)
        }
    }
    void curtirVaga(int candidatoId, int vagaId) {
        try {
            def sql = """
            INSERT INTO curtidas_candidato (candidato_id, vaga_id) 
            VALUES (?, ?)
            ON CONFLICT (candidato_id, vaga_id) DO NOTHING
        """
            db.execute(sql, [candidatoId, vagaId])
            println "Sucesso: Interesse registrado!"
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar curtida: ${e.message}", e)
        }
    }

    private Candidato mapRowToCandidato(def row) {
        List<String> sks = buscarCompetenciasPorCandidato(row.id)

        Candidato c = new Candidato(
                row.nome ?: "",
                row.email ?: "",
                row.pais ?: "Brasil",
                row.cep ?: "",
                row.descricao_pessoal ?: "",
                row.cpf ?: "",
                0,
                sks
        )
        c.id = row.id
        c.sobrenome = row.sobrenome ?: ""
        c.dataNascimento = row.data_nascimento?.toString() ?: ""
        return c
    }

    private List<String> buscarCompetenciasPorCandidato(int candidatoId) {
        def sql = """
            SELECT comp.nome 
            FROM candidato_competencias cc
            JOIN competencias comp ON cc.competencia_id = comp.id
            WHERE cc.candidato_id = ?
        """
        return db.rows(sql, [candidatoId]).collect { it.nome }
    }
}