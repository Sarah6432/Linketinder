import groovy.sql.Sql

class CompetenciaDAO {
    private final Sql db

    CompetenciaDAO(Sql sql) {
        this.db = sql
    }

    void salvar(String nomeComp) {
        try {
            db.execute("""
                INSERT INTO competencias (nome) VALUES (?) 
                ON CONFLICT (nome) DO NOTHING
            """, [nomeComp])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar competência: ${e.message}", e)
        }
    }

    void atualizar(int id, String novoNome) {
        try {
            db.executeUpdate("""
                UPDATE competencias 
                SET nome = ? 
                WHERE id = ?
            """, [novoNome, id])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar competência ID $id: ${e.message}", e)
        }
    }

    void deletar(int id) {
        try {
            db.execute("DELETE FROM competencias WHERE id = ?", [id])
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar competência ID $id: ${e.message}", e)
        }
    }

    List<Map> listar() {
        try {
            return db.rows("SELECT * FROM competencias ORDER BY nome ASC")
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar competências: ${e.message}", e)
        }
    }
}