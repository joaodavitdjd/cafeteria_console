package pedido;

import java.util.ArrayList;
import java.util.List;
import produto.Produto;

public class Pedido {
    private int numero;
    private String cliente;
    private StatusPedido status = StatusPedido.EM_PREPARO;
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido(int numero, String cliente) {
        this.numero = numero;
        this.cliente = cliente;
    }

    public int getNumero()          { return numero; }
    public String getCliente()      { return cliente; }
    public StatusPedido getStatus() { return status; }

    public void marcarComoPronto() {
        this.status = StatusPedido.PRONTO;
    }

    public boolean adicionarItem(Produto produto, int quantidade) {
        if (quantidade <= 0 || quantidade > produto.getEstoque()) {
            return false;
        }
        produto.setEstoque(produto.getEstoque() - quantidade);
        itens.add(new ItemPedido(produto, quantidade));
        return true;
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public double getTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void exibirResumo() {
        System.out.println(String.format("Pedido #%03d | Cliente: %s | %s", numero, cliente, status));
        for (ItemPedido item : itens) {
            item.exibirInformacoes();
        }
        System.out.println("Total: R$ " + String.format("%.2f", getTotal()));
    }
}