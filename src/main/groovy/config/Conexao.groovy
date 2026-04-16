package config

import groovy.sql.Sql

class Conexao {
    private static Sql instance

    static Sql getConn() {
        if (instance == null) {
            try {
                String url = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/linketinder"
                String user = System.getenv("DB_USER") ?: "postgres"
                String pass = System.getenv("DB_PASS")
                String driver = "org.postgresql.Driver"

                if (!pass) {
                    throw new RuntimeException("A variavel de ambiente DB_PASS nao foi configurada.")
                }

                instance = Sql.newInstance(url, user, pass, driver)

            } catch (Exception e) {
                e.printStackTrace()
            }
        }
        return instance
    }
}