package pedido;

import java.util.ArrayList;
import java.util.List;
import produto.Produto;

public class Pedido {
    private String cliente;
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido(String cliente) {
        this.cliente = cliente;
    }
    public String getCliente() { return cliente; }

    public boolean adicionarItem(Produto produto, int quantidade) {
        if (quantidade <= 0 || quantidade > produto.getEstoque()) {
            return false;
        }
        produto.setEstoque(produto.getEstoque() - quantidade);
        itens.add(new ItemPedido(produto, quantidade));
        return true;
    }

    public boolean estaVazio(){
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
        System.out.println("Cliente: " + cliente);
        for (ItemPedido item : itens) {
            item.exibirInformacoes();
        }
        System.out.println("Total: R$ " + String.format("%.2f", getTotal()));
    }
}
