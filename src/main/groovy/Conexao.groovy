import groovy.sql.Sql

class Conexao {
    static def getConn() {
        return Sql.newInstance(
                "jdbc:postgresql://localhost:5432/linketinder",
                "postgres",
                "0472",
                "org.postgresql.Driver"
        )
    }
}