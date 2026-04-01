package model

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

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
            List params = [cand.nome, cand.sobrenome, cand.dataNascimento, cand.email, cand.cpf, cand.pais, cand.cep, cand.descricao, cand.senha]

            def keys = db.executeInsert(sql, params)

            if (keys && keys[0] && keys[0][0]) {
                return keys[0][0] as int
            }
            return -1
        } catch (Exception e) {
            println ">> [DATABASE ERROR]: ${e.message}"
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
            return db.rows("SELECT * FROM candidatos ORDER BY id ASC").collect { mapRowToCandidato(it) }
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

    void atualizarCompetencias(int candidatoId, List<String> nomesCompetencias) {
        nomesCompetencias.each { nome ->
            def row = db.firstRow("SELECT id FROM competencias WHERE LOWER(nome) = LOWER(?)", [nome.trim()])
            int compId

            if (!row) {
                def keys = db.executeInsert("INSERT INTO competencias (nome) VALUES (?)", [nome.trim()])
                compId = keys[0][0]
            } else {
                compId = row.id
            }
            db.execute("INSERT INTO candidato_competencias (candidato_id, competencia_id) VALUES (?, ?)", [candidatoId, compId])
        }
    }

    Candidato buscarPorEmail(String email) {
        try {
            GroovyRowResult row = db.firstRow("SELECT * FROM candidatos WHERE email = ?", [email])
            return row ? mapRowToCandidato(row) : null
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar email: ${e.message}")
        }
    }

    Map buscarPerfilCompleto(int candidatoId) {
        def sql = """
        SELECT c.*, 
               (SELECT STRING_AGG(comp.nome, ', ') 
                FROM candidato_competencias cc_comp 
                JOIN competencias comp ON cc_comp.competencia_id = comp.id 
                WHERE cc_comp.candidato_id = c.id) AS lista_competencias
        FROM candidatos c 
        WHERE c.id = ?
    """
        return db.firstRow(sql, [candidatoId])
    }

    private Candidato mapRowToCandidato(def row) {
        String sqlComp = "SELECT c.nome FROM candidato_competencias cc JOIN competencias c ON cc.competencia_id = c.id WHERE cc.candidato_id = ?"
        List<String> sks = db.rows(sqlComp, [row.id]).collect { it.nome }

        Candidato c = new Candidato(
                row.nome ?: "",
                row.email ?: "",
                row.pais ?: "",
                row.cep ?: "",
                row.descricao_pessoal ?: "",
                row.cpf ?: "",
                sks
        )
        c.id = row.id
        c.sobrenome = row.sobrenome ?: ""
        c.dataNascimento = row.data_nascimento?.toString() ?: ""
        c.senha = row.senha
        return c
    }
}