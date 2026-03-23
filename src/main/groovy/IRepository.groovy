interface IReader<T> {
    List<T> listarTodos()
}

interface IWriter<T> {
    int salvar(T entidade)
    void atualizar(T entidade)
    void deletar(int id)
}

interface ICurtida {
    void registrarCurtida(int origemId, int destinoId)
}

interface ICompetenciaManager {
    void vincular(int entidadeId, String nomeSkill)
}