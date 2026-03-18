import groovy.sql.Sql

class VagaDAO {
    Sql db = Conexao.getConn()

    int salvar(Vaga vaga, int empresaId) {
        try {
            def keys = db.executeInsert("""
            INSERT INTO vagas (empresa_id, nome_vaga, descricao, local_estado_cidade) 
            VALUES (?, ?, ?, ?)
        """, [empresaId, vaga.nome, vaga.descricao, vaga.localEstadoCidade])

            return keys[0][0] as int
        } catch (Exception e) {
            println "Erro ao salvar vaga: ${e.message}"
            return -1
        }
    }

    void vincularRequisito(int vagaId, String nomeCompetencia) {
        try {
            db.execute("INSERT INTO competencias (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING", [nomeCompetencia])

            db.execute("""
            INSERT INTO vagas_competencias (vaga_id, competencia_id)
            SELECT ?, id FROM competencias WHERE nome = ?
            ON CONFLICT DO NOTHING
        """, [vagaId, nomeCompetencia])
        } catch (Exception e) {
            println "Erro ao vincular requisito $nomeCompetencia: ${e.message}"
        }
    }
    void atualizar(Vaga vaga) {
        try {
            String sql = "UPDATE vagas SET nome_vaga = ?, descricao = ?, local_estado_cidade = ? WHERE id = ?"
            db.executeUpdate(sql, [vaga.nome, vaga.descricao, vaga.local_estado_cidade, vaga.id])
            println "Sucesso: Vaga atualizada!"
        } catch (Exception e) {
            println "Erro ao atualizar: ${e.message}"
        }
    }

    void deletar(int vagaId) {
        try {
            db.execute("DELETE FROM vagas WHERE id = ?", [vagaId])
            println "Sucesso: Vaga deletada!"
        } catch (Exception e) {
            println "Erro ao deletar: ${e.message}"
        }
    }
    List<Vaga> listarTodas() {
        List<Vaga> lista = []
        try {
            db.eachRow("""
            SELECT v.*, e.nome_empresa 
            FROM vagas v 
            JOIN empresas e ON v.empresa_id = e.id
            ORDER BY v.id ASC
        """) { row ->
                List<String> reqs = []
                db.eachRow("""
                SELECT c.nome 
                FROM vagas_competencias vc
                JOIN competencias c ON vc.competencia_id = c.id
                WHERE vc.vaga_id = ${row.id}
            """) { compRow ->
                    reqs.add(compRow.nome)
                }

                Empresa emp = new Empresa(row.nome_empresa, "", "", "", "", "", "", [])
                emp.id = row.empresa_id

                Vaga v = new Vaga(row.nome_vaga, row.descricao, reqs, emp)
                v.id = row.id
                v.localEstadoCidade = row.local_estado_cidade
                lista.add(v)
            }
        } catch (Exception e) {
            println "Erro ao listar vagas: ${it.message}"
        }
        return lista
    }
}