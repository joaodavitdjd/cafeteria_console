package banco;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import produto.Categoria;
import produto.Produto;

public class ProdutoRepositorio {

    public void salvar(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, preco, estoque, categoria) VALUES (?, ?, ?, ?)";
        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, produto.getNome());
            ps.setBigDecimal(2, BigDecimal.valueOf(produto.getPreco()));
            ps.setInt(3, produto.getEstoque());
            ps.setString(4, produto.getCategoria().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt("id"));
                }
            }
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id, nome, preco, estoque, categoria FROM produtos ORDER BY id";
        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                produtos.add(criarProduto(rs));
            }
        }
        return produtos;
    }

    public Produto buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT id, nome, preco, estoque, categoria FROM produtos WHERE LOWER(nome) = LOWER(?)";
        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return criarProduto(rs);
                }
            }
        }
        return null;
    }

    public void atualizarEstoque(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET estoque = ? WHERE id = ?";
        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, produto.getEstoque());
            ps.setInt(2, produto.getId());
            ps.executeUpdate();
        }
    }

    private Produto criarProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getDouble("preco"),
                rs.getInt("estoque"),
                Categoria.valueOf(rs.getString("categoria")));
    }
    public boolean temPedidos(Produto produto) throws SQLException {
        String sql = "SELECT 1 FROM itens_pedido WHERE produto_id = ? LIMIT 1";
        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, produto.getId());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void excluir(Produto produto) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, produto.getId());
            ps.executeUpdate();
        }
    }
}