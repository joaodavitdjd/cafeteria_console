package pedido;

import produto.Produto;

public class ItemPedido {
    private Produto produto;
    private int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }

    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }

    public void exibirInformacoes(){
        System.out.println("  " + quantidade + "x " + produto.getNome()
                + " | R$ " + String.format("%.2f", produto.getPreco())
                + " cada | subtotal: R$ " + String.format("%.2f", getSubtotal()));
    }
}