package DAO

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import model.Empresa
import model.ICurtida
import model.IReader
import model.IWriter

class EmpresaDAO implements IReader<Empresa>, IWriter<Empresa>, ICurtida {
    private final Sql db

    EmpresaDAO(Sql sql) {
        this.db = sql
    }

    @Override
    int salvar(Empresa emp) {
        try {
            String sql = """
                INSERT INTO empresas (nome_empresa, email_corporativo, cnpj, cep, pais, descricao, senha) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """
            List<Object> params = [emp.nome, emp.email, emp.cnpj, emp.cep, emp.pais, emp.descricao, emp.senha]
            List<List<Object>> keys = db.executeInsert(sql, params)
            return keys[0][0] as int
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar empresa: ${e.message}", e)
        }
    }

    @Override
    void atualizar(Empresa emp) {
        try {
            String sql = """
                UPDATE empresas SET nome_empresa = ?, email_corporativo = ?, cnpj = ?, cep = ?, pais = ?, descricao = ? 
                WHERE id = ?
            """
            db.executeUpdate(sql, [emp.nome, emp.email, emp.cnpj, emp.cep, emp.pais, emp.descricao, emp.id])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar empresa: ${e.message}", e)
        }
    }

    @Override
    void deletar(int id) {
        db.execute("DELETE FROM empresas WHERE id = ?", [id])
    }

    @Override
    List<Empresa> listarTodos() {
        return db.rows("SELECT * FROM empresas ORDER BY id ASC").collect { Object row -> mapRowToEmpresa(row) }
    }

    @Override
    void registrarCurtida(int empresaId, int candidatoId) {
        db.execute("INSERT INTO curtidas_empresa (empresa_id, candidato_id) VALUES (?, ?) ON CONFLICT DO NOTHING", [empresaId, candidatoId])
    }

    List<Map> listarCandidatosInteressados(int empresaId) {
        String sql = """
            SELECT 
                c.id AS id, 
                c.nome AS nome, 
                v.nome_vaga AS nome_vaga,
                (SELECT STRING_AGG(comp.nome, ', ') 
                 FROM candidato_competencias cc_comp 
                 JOIN competencias comp ON cc_comp.competencia_id = comp.id 
                 WHERE cc_comp.candidato_id = c.id) AS competencias
            FROM curtidas_candidato cc
            JOIN candidatos c ON cc.candidato_id = c.id
            JOIN vagas v ON cc.vaga_id = v.id
            WHERE v.empresa_id = ?
        """
        return db.rows(sql, [empresaId]) ?: []
    }

    List<Map> listarMatchesReais(int empresaId) {
        try {
            String sql = """
            SELECT DISTINCT 
                c.id AS candidatoid,
                c.nome AS nome, 
                c.sobrenome AS sobrenome,
                c.email AS email,
                c.cpf AS cpf,
                c.descricao_pessoal AS bio,
                c.pais AS pais,
                v.nome_vaga AS vaga,
                (SELECT STRING_AGG(comp.nome, ', ') 
                 FROM candidato_competencias cc_comp 
                 JOIN competencias comp ON cc_comp.competencia_id = comp.id 
                 WHERE cc_comp.candidato_id = c.id) AS competencias
            FROM curtidas_candidato cc
            INNER JOIN vagas v ON cc.vaga_id = v.id
            INNER JOIN curtidas_empresa ce ON ce.candidato_id = cc.candidato_id
            INNER JOIN candidatos c ON c.id = cc.candidato_id
            WHERE v.empresa_id = ? 
            AND ce.empresa_id = ?
        """
            List<Map> rows = db.rows(sql, [empresaId, empresaId])
            return rows ?: []
        } catch (Exception e) {
            e.printStackTrace()
            return []
        }
    }

    List<Integer> listarIdsCandidatosComMatch(int empresaId) {
        try {
            String sql = """
            SELECT DISTINCT cc.candidato_id
            FROM curtidas_candidato cc
            INNER JOIN vagas v ON cc.vaga_id = v.id
            INNER JOIN curtidas_empresa ce ON ce.candidato_id = cc.candidato_id
            WHERE v.empresa_id = ? 
            AND ce.empresa_id = ?
        """
            return db.rows(sql, [empresaId, empresaId]).collect { it.candidato_id as int } ?: []
        } catch (Exception e) {
            e.printStackTrace()
            return []
        }
    }

    Empresa buscarPorEmail(String email){
        try{
            GroovyRowResult row = db.firstRow("SELECT * FROM empresas WHERE email_corporativo = ?", [email])
            return row ? mapRowToEmpresa(row) : null
        } catch (Exception e){
            throw new RuntimeException("Erro ao buscar email da empresa: ${e.message}")
        }
    }

    private Empresa mapRowToEmpresa(Object row) {
        Empresa e = new Empresa(
                row.nome_empresa?.toString() ?: "",
                row.email_corporativo?.toString() ?: "",
                row.pais?.toString() ?: "",
                row.cep?.toString() ?: "",
                row.descricao?.toString() ?: "",
                row.cnpj?.toString() ?: ""
        )
        e.id = row.id as int
        e.senha = row.senha?.toString()
        e.competencias = []
        return e
    }
}