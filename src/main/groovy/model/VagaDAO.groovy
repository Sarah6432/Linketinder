package model

import groovy.sql.Sql

class VagaDAO implements IReader<Vaga>, IWriter<Vaga>, ICompetenciaManager {
    private final Sql db

    VagaDAO(Sql sql) {
        this.db = sql
    }

    @Override
    int salvar(Vaga vaga) {
        return salvar(vaga, vaga.empresa.id)
    }

    int salvar(Vaga vaga, int empresaId) {
        try {
            def sql = "INSERT INTO vagas (nome_vaga, descricao, local_estado_cidade, empresa_id) VALUES (?, ?, ?, ?)"
            def params = [vaga.nome, vaga.descricao, vaga.localEstadoCidade, empresaId]
            def keys = db.executeInsert(sql, params)
            return keys[0][0] as int
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar vaga: ${e.message}", e)
        }
    }

    @Override
    void atualizar(Vaga vaga) {
        try {
            def sql = "UPDATE vagas SET nome_vaga = ?, descricao = ?, local_estado_cidade = ? WHERE id = ?"
            db.executeUpdate(sql, [vaga.nome, vaga.descricao, vaga.localEstadoCidade, vaga.id])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar vaga: ${e.message}", e)
        }
    }

    @Override
    void deletar(int id) {
        db.execute("DELETE FROM vagas WHERE id = ?", [id])
    }

    @Override
    List<Vaga> listarTodos() {
        try {
            def sql = """
                SELECT v.*, e.nome_empresa as emp_nome 
                FROM vagas v 
                JOIN empresas e ON v.empresa_id = e.id
                ORDER BY v.id ASC
            """
            return db.rows(sql).collect { mapRowToVaga(it) }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar vagas: ${e.message}", e)
        }
    }

    void limparCompetenciasVaga(int vagaId) {
        db.execute("DELETE FROM vagas_competencias WHERE vaga_id = ?", [vagaId])
    }

    void vincularCompetenciaVaga(int vagaId, String nomeSkill) {
        try {
            db.execute("INSERT INTO competencias (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING", [nomeSkill])

            db.execute("""
            INSERT INTO vagas_competencias (vaga_id, competencia_id) 
            SELECT ?, id FROM competencias WHERE nome = ? 
            ON CONFLICT DO NOTHING
        """, [vagaId, nomeSkill])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao vincular requisito: ${e.message}")
        }
    }

    void atualizarBasico(int id, String nome, String descricao) {
        db.execute("UPDATE vagas SET nome_vaga = ?, descricao = ? WHERE id = ?", [nome, descricao, id])
    }

    private Vaga mapRowToVaga(def row) {
        def sqlComp = """
        SELECT c.nome 
        FROM vagas_competencias vc 
        JOIN competencias c ON vc.competencia_id = c.id 
        WHERE vc.vaga_id = ?
    """
        List<String> listaCompetencias = db.rows(sqlComp, [row.id]).collect { it.nome }

        Empresa emp = new Empresa(row.emp_nome ?: "", "", "", "", "", "")
        emp.id = row.empresa_id

        Vaga v = new Vaga(
                row.nome_vaga ?: "",
                row.descricao ?: "",
                listaCompetencias,
                emp
        )
        v.id = row.id
        v.localEstadoCidade = row.local_estado_cidade ?: ""

        return v
    }
}