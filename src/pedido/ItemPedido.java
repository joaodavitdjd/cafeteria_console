package pedido;

import produto.Produto;

public class ItemPedido {
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    // item novo: copia o preço atual do produto
    public ItemPedido(Produto produto, int quantidade) {
        this(produto, quantidade, produto.getPreco());
    }

    // item que veio do banco: usa o preço que foi gravado na compra
    public ItemPedido(Produto produto, int quantidade, double precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Produto getProduto()      { return produto; }
    public int getQuantidade()       { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }

    public double getSubtotal() {
        return precoUnitario * quantidade;
    }

    public void exibirInformacoes() {
        System.out.println("  " + quantidade + "x " + produto.getNome()
                + " | R$ " + String.format("%.2f", precoUnitario)
                + " cada | subtotal: R$ " + String.format("%.2f", getSubtotal()));
    }
}