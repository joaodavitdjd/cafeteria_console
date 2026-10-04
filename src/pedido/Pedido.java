package pedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import produto.Produto;

public class Pedido {
    private int numero;
    private String cliente;
    private StatusPedido status;
    private List<ItemPedido> itens = new ArrayList<>();

    // pedido novo: ainda sem número, o banco gera ao salvar
    public Pedido(String cliente) {
        this.cliente = cliente;
        this.status = StatusPedido.EM_PREPARO;
    }

    // pedido que veio do banco
    public Pedido(int numero, String cliente, StatusPedido status) {
        this.numero = numero;
        this.cliente = cliente;
        this.status = status;
    }

    public int getNumero()             { return numero; }
    public String getCliente()         { return cliente; }
    public StatusPedido getStatus()    { return status; }
    public List<ItemPedido> getItens() { return Collections.unmodifiableList(itens); }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void marcarComoPronto() {
        this.status = StatusPedido.PRONTO;
    }

    // pedido novo: confere o estoque e baixa na memória
    public boolean adicionarItem(Produto produto, int quantidade) {
        if (quantidade <= 0 || quantidade > produto.getEstoque()) {
            return false;
        }
        produto.setEstoque(produto.getEstoque() - quantidade);
        itens.add(new ItemPedido(produto, quantidade));
        return true;
    }

    // item que veio do banco: o estoque já foi baixado na compra
    public void adicionarItemSalvo(ItemPedido item) {
        itens.add(item);
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