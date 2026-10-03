package estoque;

import produto.Produto;

public class RemoverEstoque {

        public void executar(Produto produto, int quantidade) {
            if (quantidade <= 0) {
                System.out.println("A quantidade deve ser maior que zero.");
                return;
            }
            if (quantidade > produto.getEstoque()) {
                System.out.println("Estoque insuficiente! Só tem " + produto.getEstoque() + " un.");
                return;
            }
            produto.setEstoque(produto.getEstoque() - quantidade);
            System.out.println("Estoque atualizado! Agora tem " + produto.getEstoque() + " un.");
        }
    }