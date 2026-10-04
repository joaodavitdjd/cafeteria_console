import banco.Conexao;
import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexao {
    public static void main(String[] args) {
        try (Connection c = Conexao.conectar()) {
            System.out.println("Conectado ao banco: " + c.getCatalog());
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}