import groovy.sql.Sql

class EmpresaDAO {
    Sql db = Conexao.getConn()

    void salvar(Empresa emp) {
        try {
            db.execute """
                INSERT INTO empresas (nome_empresa, cnpj, email_corporativo, descricao, pais, cep, senha) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """, [emp.nome, emp.cnpj, emp.email, emp.descricao, emp.pais, emp.cep, emp.senha]
        } catch (Exception e) {
            println "Erro ao salvar empresa: ${e.message}"
        }
    }

    void atualizar(Empresa emp) {
        try {
            db.executeUpdate """
                UPDATE empresas 
                SET nome_empresa = ?, cnpj = ?, email_corporativo = ?, descricao = ?, pais = ?, cep = ?
                WHERE id = ?
            """, [emp.nome, emp.cnpj, emp.email, emp.descricao, emp.pais, emp.cep, emp.id]
        } catch (Exception e) {
            println "Erro ao atualizar empresa: ${e.message}"
        }
    }

    void deletar(int empresaId) {
        try {
            db.execute "DELETE FROM empresas WHERE id = ?", [empresaId]
        } catch (Exception e) {
            println "Erro ao deletar empresa: ${e.message}"
        }
    }

    List<Empresa> listarTodas() {
        List<Empresa> lista = []
        try {
            db.eachRow("SELECT * FROM empresas ORDER BY id ASC") { row ->
                Empresa e = new Empresa(row.nome_empresa, row.email_corporativo, row.pais, row.cep, row.descricao, row.cnpj, row.pais, [])
                e.id = row.id
                e.senha = row.senha
                lista.add(e)
            }
        } catch (Exception e) {
            println "Erro ao listar empresas: ${e.message}"
        }
        return lista
    }

    void curtirCandidato(int empresaId, int candidatoId) {
        try {
            db.execute """
                INSERT INTO curtidas_empresa (empresa_id, candidato_id) 
                VALUES (?, ?) 
                ON CONFLICT (empresa_id, candidato_id) DO NOTHING
            """, [empresaId, candidatoId]
            println "Interesse registrado."
        } catch (Exception e) {
            println "Erro ao curtir candidato: ${e.message}"
        }
    }

    List listarInteressados(int empresaId) {
        try {
            return db.rows("""
                SELECT c.id AS cand_id, c.nome AS cand_nome, v.id AS vaga_id, v.nome_vaga AS vaga_nome
                FROM curtidas_candidato cc
                JOIN candidatos c ON cc.candidato_id = c.id
                JOIN vagas v ON cc.vaga_id = v.id
                WHERE v.empresa_id = ?
            """, [empresaId])
        } catch (Exception e) {
            println "Erro ao listar interessados: ${e.message}"
            return []
        }
    }

    List listarMatchesReais(int empresaId) {
        try {
            return db.rows("""
                SELECT DISTINCT c.nome AS candidato, v.nome_vaga AS vaga
                FROM curtidas_candidato cc
                JOIN curtidas_empresa ce ON cc.candidato_id = ce.candidato_id
                JOIN candidatos c ON cc.candidato_id = c.id
                JOIN vagas v ON cc.vaga_id = v.id
                WHERE v.empresa_id = ? AND ce.empresa_id = ?
            """, [empresaId, empresaId])
        } catch (Exception e) {
            println "Erro ao buscar matches: ${e.message}"
            return []
        }
    }
}