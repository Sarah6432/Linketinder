package config

import groovy.sql.Sql

class Conexao {
    private static Sql instance

    static Sql getConn() {
        if (instance == null) {
            try {
                instance = Sql.newInstance(
                        "jdbc:postgresql://localhost:5432/linketinder",
                        "postgres",
                        "0472",
                        "org.postgresql.Driver"
                )
                println "Conexão estabelecida com sucesso!"
            } catch (Exception e) {
                println "Falha ao conectar com o banco: ${e.message}"
                throw e
            }
        }
        return instance
    }

    static void fecharConexao() {
        if (instance != null) {
            instance.close()
            instance = null
            println "Conexão encerrada!"
        }
    }
}