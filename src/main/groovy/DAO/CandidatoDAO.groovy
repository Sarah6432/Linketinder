package DAO

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import model.Candidato
import model.ICompetenciaManager
import model.ICurtida
import model.IReader
import model.IWriter

class CandidatoDAO implements IReader<Candidato>, IWriter<Candidato>, ICurtida, ICompetenciaManager {
    private final Sql db

    CandidatoDAO(Sql sql) {
        this.db = sql
    }

    @Override
    int salvar(Candidato cand) {
        try {
            String sql = """
                INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao_pessoal, senha) 
                VALUES (?, ?, CAST(? AS DATE), ?, ?, ?, ?, ?, ?)
            """
            List<Object> params = [cand.nome, cand.sobrenome, cand.dataNascimento, cand.email, cand.cpf, cand.pais, cand.cep, cand.descricao, cand.senha]

            List<List<Object>> keys = db.executeInsert(sql, params)

            if (keys && keys[0] && keys[0][0]) {
                return keys[0][0] as int
            }
            return -1
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar candidato: ${e.message}")
        }
    }

    @Override
    void atualizar(Candidato cand) {
        try {
            String sql = """
                UPDATE candidatos SET nome = ?, sobrenome = ?, data_nascimento = CAST(? AS DATE), 
                email = ?, cpf = ?, pais = ?, cep = ?, descricao_pessoal = ? WHERE id = ?
            """
            db.executeUpdate(sql, [cand.nome, cand.sobrenome, cand.dataNascimento, cand.email, cand.cpf, cand.pais, cand.cep, cand.descricao, cand.id])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar: ${e.message}")
        }
    }

    @Override
    void deletar(int id) {
        try {
            db.execute("DELETE FROM candidatos WHERE id = ?", [id])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar candidato: ${e.message}")
        }
    }

    @Override
    List<Candidato> listarTodos() {
        try {
            return db.rows("SELECT * FROM candidatos ORDER BY id ASC").collect { Object row -> mapRowToCandidato(row) }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar candidatos: ${e.message}")
        }
    }

    @Override
    void registrarCurtida(int candidatoId, int vagaId) {
        try {
            db.execute("INSERT INTO curtidas_candidato (candidato_id, vaga_id) VALUES (?, ?) ON CONFLICT DO NOTHING", [candidatoId, vagaId])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar curtida: ${e.message}")
        }
    }

    @Override
    void vincularCompetencia(int candidatoId, String nomeSkill) {
        try {
            int compId = buscarOuCriarCompetencia(nomeSkill)
            db.execute("""
                INSERT INTO candidato_competencias (candidato_id, competencia_id) 
                VALUES (?, ?) ON CONFLICT DO NOTHING
            """, [candidatoId, compId])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao vincular competência: ${e.message}")
        }
    }

    @Override
    void desvincularCompetencia(int candidatoId, String nomeSkill) {
        try {
            db.execute("""
                 DELETE FROM candidato_competencias 
                 WHERE candidato_id = ? 
                 AND competencia_id IN (SELECT id FROM competencias WHERE LOWER(nome) = LOWER(?))""",
                 [candidatoId, nomeSkill.trim()])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao desvincular competência: ${e.message}")
        }
    }

    @Override
    void atualizarCompetenciasNoBanco(int candidatoId, List<String> novasSkills) {
        try {
            db.withTransaction {
                db.execute("DELETE FROM candidato_competencias WHERE candidato_id = ?", [candidatoId])
                novasSkills.each { String skill ->
                    vincularCompetencia(candidatoId, skill)
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar competências em lote: ${e.message}")
        }
    }

    private int buscarOuCriarCompetencia(String nome) {
        String n = nome.trim()
        GroovyRowResult row = db.firstRow("SELECT id FROM competencias WHERE LOWER(nome) = LOWER(?)", [n])
        if (row) return row.id as int

        List<List<Object>> keys = db.executeInsert("INSERT INTO competencias (nome) VALUES (?)", [n])
        return keys[0][0] as int
    }

    Candidato buscarPorEmail(String email) {
        try {
            GroovyRowResult row = db.firstRow("SELECT * FROM candidatos WHERE email = ?", [email])
            return row ? mapRowToCandidato(row) : null
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar email: ${e.message}")
        }
    }

    private Candidato mapRowToCandidato(Object row) {
        String sqlComp = "SELECT c.nome FROM candidato_competencias cc JOIN competencias c ON cc.competencia_id = c.id WHERE cc.candidato_id = ?"
        List<String> sks = db.rows(sqlComp, [row.id]).collect { Object it -> it.nome.toString() }

        Candidato c = new Candidato(
                row.nome?.toString() ?: "",
                row.email?.toString() ?: "",
                row.pais?.toString() ?: "",
                row.cep?.toString() ?: "",
                row.descricao_pessoal?.toString() ?: "",
                row.cpf?.toString() ?: "",
                sks
        )
        c.id = row.id as int
        c.sobrenome = row.sobrenome?.toString() ?: ""
        c.dataNascimento = row.data_nascimento?.toString() ?: ""
        c.senha = row.senha?.toString()
        return c
    }
}