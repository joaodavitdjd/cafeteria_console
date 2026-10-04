package produto;

public class Produto {
    private String nome;
    private double preco;
    private int estoque;
    private Categoria categoria;
    private int id;

    public Produto(String nome, double preco, int estoque, Categoria categoria) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
    }
    public Produto(int id, String nome, double preco, int estoque, Categoria categoria) {
        this(nome, preco, estoque, categoria);
        this.id = id;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() {return nome;}

    public double getPreco() {return preco;}

    public int getEstoque() {return estoque;}
    public void setEstoque(int estoque) {
        if (estoque < 0) {
            System.out.println("O estoque não pode ser negativo.");
            return;
        }
        this.estoque = estoque;
    }

    public Categoria getCategoria() { return categoria; }

    public void exibirInformacoes() {
        System.out.println("Categoria: " + categoria + " | Produto: " + nome + " | preço: R$ " + String.format("%.2f", preco)
                + " | Estoque: " + estoque + " un");
    }
}

