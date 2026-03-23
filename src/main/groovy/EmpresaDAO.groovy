import groovy.sql.Sql

class EmpresaDAO implements IReader<Empresa>, IWriter<Empresa>, ICurtida {
    private final Sql db

    EmpresaDAO(Sql sql) {
        this.db = sql
    }

    @Override
    int salvar(Empresa emp) {
        try {
            def sql = """
                INSERT INTO empresas (nome_empresa, email_corporativo, cnpj, cep, pais, descricao, senha) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """
            def params = [emp.nome, emp.email, emp.cnpj, emp.cep, emp.pais, emp.descricao, emp.senha]
            def keys = db.executeInsert(sql, params)
            return keys[0][0] as int
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar empresa: ${e.message}", e)
        }
    }

    @Override
    void atualizar(Empresa emp) {
        try {
            def sql = """
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
        return db.rows("SELECT * FROM empresas ORDER BY id ASC").collect { mapRowToEmpresa(it) }
    }

    @Override
    void registrarCurtida(int empresaId, int candidatoId) {
        db.execute("INSERT INTO curtidas_empresa (empresa_id, candidato_id) VALUES (?, ?) ON CONFLICT DO NOTHING", [empresaId, candidatoId])
    }

    List<Map> listarInteressados(int empresaId) {
        def sql = """
            SELECT c.id as cand_id, c.nome as cand_nome, v.nome_vaga as vaga_nome
            FROM curtidas_candidato cc
            JOIN candidatos c ON cc.candidato_id = c.id
            JOIN vagas v ON cc.vaga_id = v.id
            WHERE v.empresa_id = ?
        """
        return db.rows(sql, [empresaId])
    }

    List<Map> listarMatchesReais(int empresaId) {
        def sql = """
            SELECT DISTINCT c.nome as candidato, v.nome_vaga as vaga
            FROM curtidas_candidato cc
            JOIN curtidas_empresa ce ON cc.candidato_id = ce.candidato_id
            JOIN candidatos c ON cc.candidato_id = c.id
            JOIN vagas v ON cc.vaga_id = v.id
            WHERE v.empresa_id = ? AND ce.empresa_id = ?
        """
        return db.rows(sql, [empresaId, empresaId])
    }

    private Empresa mapRowToEmpresa(def row) {
        Empresa e = new Empresa(
                row.nome_empresa ?: "",
                row.email_corporativo ?: "",
                row.cep ?: "",
                row.pais ?: "",
                row.descricao ?: "",
                row.cnpj ?: ""
        )
        e.id = row.id
        e.senha = row.senha
        return e
    }
}