package DAO

import groovy.sql.Sql
import model.Empresa
import model.ICompetenciaManager
import model.IReader
import model.IWriter
import model.Vaga

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
            String sql = "INSERT INTO vagas (nome_vaga, descricao, local_estado_cidade, empresa_id) VALUES (?, ?, ?, ?)"
            List params = [vaga.nome, vaga.descricao, vaga.localEstadoCidade, empresaId]
            List<List<Object>> keys = db.executeInsert(sql, params)
            return keys[0][0] as int
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar vaga: ${e.message}", e)
        }
    }

    @Override
    void atualizar(Vaga vaga) {
        try {
            String sql = "UPDATE vagas SET nome_vaga = ?, descricao = ?, local_estado_cidade = ? WHERE id = ?"
            db.executeUpdate(sql, [vaga.nome, vaga.descricao, vaga.localEstadoCidade, vaga.id])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar vaga: ${e.message}", e)
        }
    }

    @Override
    void deletar(int id) {
        try {
            db.execute("DELETE FROM vagas WHERE id = ?", [id])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar vaga: ${e.message}", e)
        }
    }

    @Override
    List<Vaga> listarTodos() {
        try {
            String sql = """
                SELECT v.*, e.nome_empresa as emp_nome 
                FROM vagas v 
                JOIN empresas e ON v.empresa_id = e.id
                ORDER BY v.id ASC
            """
            return db.rows(sql).collect { Map row -> mapRowToVaga(row) }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar vagas: ${e.message}", e)
        }
    }

    @Override
    void vincularCompetencia(int vagaId, String nomeSkill) {
        try {
            int compId = buscarOuCriarCompetencia(nomeSkill)
            db.execute("""
                INSERT INTO vagas_competencias (vaga_id, competencia_id) 
                VALUES (?, ?) ON CONFLICT DO NOTHING
            """, [vagaId, compId])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao vincular competência à vaga: ${e.message}")
        }
    }

    @Override
    void desvincularCompetencia(int vagaId, String nomeSkill) {
        try {
            db.execute("""
                DELETE FROM vagas_competencias 
                WHERE vaga_id = ? AND competencia_id = (SELECT id FROM competencias WHERE LOWER(nome) = LOWER(?))
            """, [vagaId, nomeSkill.trim()])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao desvincular competência da vaga: ${e.message}")
        }
    }

    @Override
    void atualizarCompetenciasNoBanco(int vagaId, List<String> novasSkills) {
        try {
            db.withTransaction {
                db.execute("DELETE FROM vagas_competencias WHERE vaga_id = ?", [vagaId])
                novasSkills.each { String skill ->
                    vincularCompetencia(vagaId, skill)
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar competências da vaga: ${e.message}")
        }
    }

    private int buscarOuCriarCompetencia(String nome) {
        String n = nome.trim()
        Object row = db.firstRow("SELECT id FROM competencias WHERE LOWER(nome) = LOWER(?)", [n])
        if (row) return row.id as int

        List<List<Object>> keys = db.executeInsert("INSERT INTO competencias (nome) VALUES (?)", [n])
        return keys[0][0] as int
    }

    void atualizarBasico(int id, String nome, String descricao) {
        db.execute("UPDATE vagas SET nome_vaga = ?, descricao = ? WHERE id = ?", [nome, descricao, id])
    }

    private Vaga mapRowToVaga(Object row) {
        String sqlComp = """
            SELECT c.nome 
            FROM vagas_competencias vc 
            JOIN competencias c ON vc.competencia_id = c.id 
            WHERE vc.vaga_id = ?
        """
        List<String> listaCompetencias = db.rows(sqlComp, [row.id]).collect { Object it -> it.nome.toString() }

        Empresa emp = new Empresa()
        emp.nome = row.emp_nome?.toString() ?: ""
        emp.id = row.empresa_id as int

        Vaga v = new Vaga(
                row.nome_vaga?.toString() ?: "",
                row.descricao?.toString() ?: "",
                listaCompetencias,
                emp
        )
        v.id = row.id as int
        v.localEstadoCidade = row.local_estado_cidade?.toString() ?: ""

        return v
    }
}