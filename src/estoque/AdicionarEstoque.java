package estoque;

import produto.Produto;

public class AdicionarEstoque {
    public void executar(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            System.out.println("A quantidade deve ser maior que zero.");
            return;
        }
        produto.setEstoque(produto.getEstoque() + quantidade);
        System.out.println("Estoque atualizado! Agora tem " + produto.getEstoque() + " un.");
    }
}

