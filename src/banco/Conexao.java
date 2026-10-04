package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:postgresql://localhost:5432/cafeteria_console";
    private static final String USUARIO = "postgres";

    public static Connection conectar() throws SQLException {
        String senha = System.getenv("DB_SENHA");
        if (senha == null) {
            throw new SQLException("Defina a variável de ambiente DB_SENHA com a senha do PostgreSQL.");
        }
        return DriverManager.getConnection(URL, USUARIO, senha);
    }
}
