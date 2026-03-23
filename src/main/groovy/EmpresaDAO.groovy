import groovy.sql.Sql

class EmpresaDAO {
    private final Sql db

    EmpresaDAO(Sql sql) {
        this.db = sql
    }

    void salvar(Empresa emp) {
        try {
            def sql = """
                INSERT INTO empresas (nome_empresa, cnpj, email_corporativo, descricao, pais, cep, senha) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """
            def params = [emp.nome, emp.cnpj, emp.email, emp.descricao, emp.pais, emp.cep, emp.senha]
            db.execute(sql, params)
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar empresa: ${e.message}", e)
        }
    }

    void atualizar(Empresa emp) {
        try {
            def sql = """
                UPDATE empresas 
                SET nome_empresa = ?, cnpj = ?, email_corporativo = ?, descricao = ?, pais = ?, cep = ?
                WHERE id = ?
            """
            def params = [emp.nome, emp.cnpj, emp.email, emp.descricao, emp.pais, emp.cep, emp.id]
            db.executeUpdate(sql, params)
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar empresa ID ${emp.id}: ${e.message}", e)
        }
    }

    void deletar(int empresaId) {
        try {
            db.execute("DELETE FROM empresas WHERE id = ?", [empresaId])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar empresa ID $empresaId: ${e.message}", e)
        }
    }

    List<Empresa> listarTodas() {
        try {
            return db.rows("SELECT * FROM empresas ORDER BY id ASC").collect { row ->
                mapRowToEmpresa(row)
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar empresas: ${e.message}", e)
        }
    }

    void curtirCandidato(int empresaId, int candidatoId) {
        try {
            def sql = """
                INSERT INTO curtidas_empresa (empresa_id, candidato_id) 
                VALUES (?, ?) 
                ON CONFLICT (empresa_id, candidato_id) DO NOTHING
            """
            db.execute(sql, [empresaId, candidatoId])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar curtida da empresa $empresaId: ${e.message}", e)
        }
    }

    List listarInteressados(int empresaId) {
        try {
            def sql = """
                SELECT c.id AS cand_id, c.nome AS cand_nome, v.id AS vaga_id, v.nome_vaga AS vaga_nome
                FROM curtidas_candidato cc
                JOIN candidatos c ON cc.candidato_id = c.id
                JOIN vagas v ON cc.vaga_id = v.id
                WHERE v.empresa_id = ?
            """
            return db.rows(sql, [empresaId])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar interessados: ${e.message}", e)
        }
    }

    List listarMatchesReais(int empresaId) {
        try {
            def sql = """
                SELECT DISTINCT c.nome AS candidato, v.nome_vaga AS vaga
                FROM curtidas_candidato cc
                JOIN curtidas_empresa ce ON cc.candidato_id = ce.candidato_id
                JOIN candidatos c ON cc.candidato_id = c.id
                JOIN vagas v ON cc.vaga_id = v.id
                WHERE v.empresa_id = ? AND ce.empresa_id = ?
            """
            return db.rows(sql, [empresaId, empresaId])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar matches: ${e.message}", e)
        }
    }

    private Empresa mapRowToEmpresa(def row) {
        Empresa e = new Empresa(
                row.nome_empresa ?: "",
                row.email_corporativo ?: "",
                row.pais ?: "Brasil",
                row.cep ?: "",
                row.descricao ?: "",
                row.cnpj ?: "",
                row.pais ?: "Brasil",
                []
        )
        e.id = row.id
        e.senha = row.senha
        return e
    }
}