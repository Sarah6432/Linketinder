import groovy.sql.Sql

class CandidatoDAO {
    Sql db = Conexao.getConn()

    int salvar(Candidato cand) {
        try {
            def keys = db.executeInsert("""
                INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao_pessoal, senha) 
                VALUES (?, ?, CAST(? AS DATE), ?, ?, ?, ?, ?, ?)
            """, [
                    cand.nome,
                    cand.sobrenome,
                    cand.dataNascimento,
                    cand.email,
                    cand.cpf,
                    cand.pais,
                    cand.cep,
                    cand.descricao,
                    cand.senha
            ])
            return keys[0][0] as int
        } catch (Exception e) {
            println "Erro ao salvar candidato: ${e.message}"
            return -1
        }
    }

    void atualizar(Candidato cand) {
        try {
            db.executeUpdate("""
                UPDATE candidatos 
                SET nome = ?, sobrenome = ?, data_nascimento = CAST(? AS DATE), 
                    email = ?, cpf = ?, pais = ?, cep = ?, descricao_pessoal = ?
                WHERE id = ?
            """, [
                    cand.nome,
                    cand.sobrenome,
                    cand.dataNascimento,
                    cand.email,
                    cand.cpf,
                    cand.pais,
                    cand.cep,
                    cand.descricao,
                    cand.id
            ])
            println "Sucesso: Candidato ID ${cand.id} atualizado!"
        } catch (Exception e) {
            println "Erro ao atualizar candidato: ${e.message}"
        }
    }

    void deletar(int candidatoId) {
        try {
            db.execute("DELETE FROM candidatos WHERE id = ?", [candidatoId])
            println "Sucesso: Candidato ID $candidatoId removido do banco!"
        } catch (Exception e) {
            println "Erro ao deletar candidato: ${e.message}"
        }
    }

    void vincularCompetencia(int candidatoId, String nomeSkill) {
        try {
            db.execute("INSERT INTO competencias (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING", [nomeSkill])

            db.execute("""
                INSERT INTO candidato_competencias (candidato_id, competencia_id)
                SELECT ?, id FROM competencias WHERE nome = ?
                ON CONFLICT DO NOTHING
            """, [candidatoId, nomeSkill])
        } catch (Exception e) {
            println "Erro ao vincular skill $nomeSkill: ${e.message}"
        }
    }

    List<Candidato> listarTodos() {
        List<Candidato> lista = []
        try {
            db.eachRow("SELECT * FROM candidatos ORDER BY id ASC") { row ->
                List<String> sks = []

                db.eachRow("""
                    SELECT comp.nome 
                    FROM candidato_competencias cc
                    JOIN competencias comp ON cc.competencia_id = comp.id
                    WHERE cc.candidato_id = ${row.id}
                """) { compRow ->
                    sks.add(compRow.nome)
                }

                Candidato c = new Candidato(
                        row.nome ?: "",
                        row.email ?: "",
                        "CEP: ${row.cep}",
                        row.cep ?: "",
                        row.descricao_pessoal ?: "",
                        row.cpf ?: "",
                        0,
                        sks
                )

                c.id = row.id
                c.sobrenome = row.sobrenome ?: ""
                c.pais = row.pais ?: "Brasil"
                c.dataNascimento = row.data_nascimento ? row.data_nascimento.toString() : ""

                lista.add(c)
            }
        } catch (Exception e) {
            println "Erro ao listar: ${e.message}"
        }
        return lista
    }

    void curtirVaga(int candidatoId, int vagaId) {
        try {
            db.execute """
            INSERT INTO curtidas_candidato (candidato_id, vaga_id) 
            VALUES (?, ?)
            ON CONFLICT DO NOTHING
        """, [candidatoId, vagaId]
            println "Sucesso: Interesse registrado no banco de dados!"
        } catch (Exception e) {
            println "Erro ao registrar curtida: ${e.message}"
        }
    }

    void desvincularCompetencia(int candidatoId, String nomeSkill) {
        try {
            String sql = """
            DELETE FROM candidato_competencias 
            WHERE candidato_id = ? 
            AND competencia_id = (SELECT id FROM competencias WHERE nome = ? LIMIT 1)
        """

            int linhasAfetadas = db.executeUpdate(sql, [candidatoId, nomeSkill])

            if (linhasAfetadas > 0) {
                println "Sucesso: A competência '$nomeSkill' foi removida do seu perfil."
            } else {
                println "Aviso: Você não possui a competência '$nomeSkill' vinculada ou ela não existe."
            }
        } catch (Exception e) {
            println "Erro técnico ao desvincular: ${e.message}"
        }
    }
}