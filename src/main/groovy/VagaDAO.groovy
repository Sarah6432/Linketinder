import groovy.sql.Sql

class VagaDAO {
    private final Sql db

    VagaDAO(Sql sql) {
        this.db = sql
    }

    int salvar(Vaga vaga, int empresaId) {
        try {
            def sql = """
                INSERT INTO vagas (empresa_id, nome_vaga, descricao, local_estado_cidade) 
                VALUES (?, ?, ?, ?)
            """
            def params = [empresaId, vaga.nome, vaga.descricao, vaga.localEstadoCidade]
            def keys = db.executeInsert(sql, params)
            return keys[0][0] as int
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar vaga: ${e.message}", e)
        }
    }

    void vincularRequisito(int vagaId, String nomeCompetencia) {
        try {
            db.execute("INSERT INTO competencias (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING", [nomeCompetencia])

            def sqlVinculo = """
                INSERT INTO vagas_competencias (vaga_id, competencia_id)
                SELECT ?, id FROM competencias WHERE nome = ?
                ON CONFLICT DO NOTHING
            """
            db.execute(sqlVinculo, [vagaId, nomeCompetencia])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao vincular requisito $nomeCompetencia à vaga $vagaId", e)
        }
    }

    void atualizar(Vaga vaga) {
        try {
            def sql = "UPDATE vagas SET nome_vaga = ?, descricao = ?, local_estado_cidade = ? WHERE id = ?"
            def params = [vaga.nome, vaga.descricao, vaga.localEstadoCidade, vaga.id]
            db.executeUpdate(sql, params)
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar vaga ID ${vaga.id}: ${e.message}", e)
        }
    }

    void deletar(int vagaId) {
        try {
            db.execute("DELETE FROM vagas WHERE id = ?", [vagaId])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar vaga ID $vagaId: ${e.message}", e)
        }
    }

    List<Vaga> listarTodas() {
        try {
            def sql = """
                SELECT v.*, e.nome_empresa 
                FROM vagas v 
                JOIN empresas e ON v.empresa_id = e.id
                ORDER BY v.id ASC
            """
            return db.rows(sql).collect { row ->
                mapRowToVaga(row)
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar vagas: ${e.message}", e)
        }
    }

    private Vaga mapRowToVaga(def row) {
        List<String> requisitos = buscarRequisitosPorVaga(row.id)

        Empresa empresa = new Empresa(row.nome_empresa, "", "", "", "", "", "", [])
        empresa.id = row.empresa_id

        Vaga vaga = new Vaga(row.nome_vaga, row.descricao, requisitos, empresa)
        vaga.id = row.id
        vaga.localEstadoCidade = row.local_estado_cidade
        return vaga
    }

    private List<String> buscarRequisitosPorVaga(int vagaId) {
        def sql = """
            SELECT c.nome 
            FROM vagas_competencias vc
            JOIN competencias c ON vc.competencia_id = c.id
            WHERE vc.vaga_id = ?
        """
        return db.rows(sql, [vagaId]).collect { it.nome }
    }
}