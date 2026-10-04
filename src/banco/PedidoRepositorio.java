package banco;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import pedido.ItemPedido;
import pedido.Pedido;
import pedido.StatusPedido;
import produto.Categoria;
import produto.Produto;

public class PedidoRepositorio {

    public void salvar(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedidos (cliente, status) VALUES (?, ?)";
        String sqlEstoque = "UPDATE produtos SET estoque = estoque - ? WHERE id = ? AND estoque >= ?";
        String sqlItem = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection c = Conexao.conectar()) {
            c.setAutoCommit(false);
            try {
                int idPedido;
                try (PreparedStatement ps = c.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, pedido.getCliente());
                    ps.setString(2, pedido.getStatus().name());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        idPedido = rs.getInt("id");
                    }
                }

                for (ItemPedido item : pedido.getItens()) {
                    try (PreparedStatement ps = c.prepareStatement(sqlEstoque)) {
                        ps.setInt(1, item.getQuantidade());
                        ps.setInt(2, item.getProduto().getId());
                        ps.setInt(3, item.getQuantidade());
                        if (ps.executeUpdate() == 0) {
                            throw new SQLException("Estoque insuficiente para " + item.getProduto().getNome() + ".");
                        }
                    }
                    try (PreparedStatement ps = c.prepareStatement(sqlItem)) {
                        ps.setInt(1, idPedido);
                        ps.setInt(2, item.getProduto().getId());
                        ps.setInt(3, item.getQuantidade());
                        ps.setBigDecimal(4, BigDecimal.valueOf(item.getPrecoUnitario()));
                        ps.executeUpdate();
                    }
                }

                c.commit();
                pedido.setNumero(idPedido);
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    public List<Pedido> listarTodos() throws SQLException {
        Map<Integer, Pedido> pedidos = new LinkedHashMap<>();
        String sqlPedidos = "SELECT id, cliente, status FROM pedidos ORDER BY id";
        String sqlItens = "SELECT i.pedido_id, i.quantidade, i.preco_unitario, "
                + "p.id, p.nome, p.preco, p.estoque, p.categoria "
                + "FROM itens_pedido i JOIN produtos p ON p.id = i.produto_id ORDER BY i.id";

        try (Connection c = Conexao.conectar()) {
            try (PreparedStatement ps = c.prepareStatement(sqlPedidos);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    pedidos.put(id, new Pedido(id, rs.getString("cliente"),
                            StatusPedido.valueOf(rs.getString("status"))));
                }
            }

            try (PreparedStatement ps = c.prepareStatement(sqlItens);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getInt("estoque"),
                            Categoria.valueOf(rs.getString("categoria")));
                    ItemPedido item = new ItemPedido(produto, rs.getInt("quantidade"),
                            rs.getDouble("preco_unitario"));
                    pedidos.get(rs.getInt("pedido_id")).adicionarItemSalvo(item);
                }
            }
        }
        return new ArrayList<>(pedidos.values());
    }

    public void atualizarStatus(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";
        try (Connection c = Conexao.conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, pedido.getStatus().name());
            ps.setInt(2, pedido.getNumero());
            ps.executeUpdate();
        }
    }
}