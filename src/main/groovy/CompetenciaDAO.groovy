import groovy.sql.Sql

class CompetenciaDAO {
    Sql db = Conexao.getConn()

    void salvar(String nomeComp) {
        try {
            db.execute("""
                INSERT INTO competencias (nome) VALUES (?) 
                ON CONFLICT (nome) DO NOTHING
            """, [nomeComp])
        } catch (Exception e) {
            println "Erro ao salvar competência: ${e.message}"
        }
    }

    void atualizar(int id, String novoNome) {
        try {
            db.executeUpdate("""
                UPDATE competencias 
                SET nome = ? 
                WHERE id = ?
            """, [novoNome, id])
            println "Sucesso: Competência atualizada para '$novoNome'!"
        } catch (Exception e) {
            println "Erro ao atualizar competência: ${e.message}"
        }
    }

    void deletar(int id) {
        try {
            db.execute("DELETE FROM competencias WHERE id = ?", [id])
            println "Sucesso: Competência ID $id removida do banco!"
        } catch (Exception e) {
            println "Erro ao deletar competência: ${e.message}"
        }
    }

    List<Map> listar() {
        try {
            return db.rows("SELECT * FROM competencias ORDER BY nome ASC")
        } catch (Exception e) {
            println "Erro ao listar competências: ${it.message}"
            return []
        }
    }
}