package model

interface IWriter<T> {
    int salvar(T entidade)
    void atualizar(T entidade)
    void deletar(int id)
}


