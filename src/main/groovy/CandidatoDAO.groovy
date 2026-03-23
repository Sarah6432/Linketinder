import groovy.sql.Sql

class CandidatoDAO implements IReader<Candidato>, IWriter<Candidato>, ICurtida, ICompetenciaManager {
    private final Sql db

    CandidatoDAO(Sql sql) {
        this.db = sql
    }

    @Override
    int salvar(Candidato cand) {
        def sql = """
            INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao_pessoal, senha) 
            VALUES (?, ?, CAST(? AS DATE), ?, ?, ?, ?, ?, ?)
        """
        def params = [cand.nome, cand.sobrenome, cand.dataNascimento, cand.email, cand.cpf, cand.pais, cand.cep, cand.descricao, cand.senha]
        def keys = db.executeInsert(sql, params)
        return keys[0][0] as int
    }

    @Override
    void atualizar(Candidato cand) {
        def sql = """
            UPDATE candidatos SET nome = ?, sobrenome = ?, data_nascimento = CAST(? AS DATE), 
            email = ?, cpf = ?, pais = ?, cep = ?, descricao_pessoal = ? WHERE id = ?
        """
        db.executeUpdate(sql, [cand.nome, cand.sobrenome, cand.dataNascimento, cand.email, cand.cpf, cand.pais, cand.cep, cand.descricao, cand.id])
    }

    @Override
    void deletar(int id) {
        db.execute("DELETE FROM candidatos WHERE id = ?", [id])
    }

    @Override
    List<Candidato> listarTodos() {
        return db.rows("SELECT * FROM candidatos ORDER BY id ASC").collect { mapRowToCandidato(it) }
    }

    @Override
    void registrarCurtida(int candidatoId, int vagaId) {
        db.execute("INSERT INTO curtidas_candidato (candidato_id, vaga_id) VALUES (?, ?) ON CONFLICT DO NOTHING", [candidatoId, vagaId])
        println "Vaga Curtida com Sucesso!"
    }

    @Override
    void vincular(int candidatoId, String nomeSkill) {
        db.execute("INSERT INTO competencias (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING", [nomeSkill])
        db.execute("INSERT INTO candidato_competencias (candidato_id, competencia_id) SELECT ?, id FROM competencias WHERE nome = ? ON CONFLICT DO NOTHING", [candidatoId, nomeSkill])
    }

    private Candidato mapRowToCandidato(def row) {
        def sqlComp = "SELECT c.nome FROM candidato_competencias cc JOIN competencias c ON cc.competencia_id = c.id WHERE cc.candidato_id = ?"
        List<String> sks = db.rows(sqlComp, [row.id]).collect { it.nome }
        Candidato c = new Candidato(row.nome ?: "", row.email ?: "", row.pais ?: "", row.cep ?: "", row.descricao_pessoal ?: "", row.cpf ?: "", sks)
        c.id = row.id
        c.sobrenome = row.sobrenome ?: ""
        c.dataNascimento = row.data_nascimento?.toString() ?: ""
        c.senha = row.senha
        return c
    }
}