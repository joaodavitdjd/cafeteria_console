package menu;

public class Opcoes {
    public void exibirOpcoes() {
        System.out.print("""
                -------PRODUTO-------
                (1)  -> Cadastrar produto
                (2)  -> Descadastrar produto
                (3)  -> Listar produtos
                -------ESTOQUE-------
                (4)  -> Adicionar estoque
                (5)  -> Remover estoque
                -------PEDIDO--------
                (6)  -> Novo pedido
                (7)  -> Listar pedidos
                (8)  -> Painel de pedidos
                (9)  -> Marcar pedido como pronto
                ---------------------
                (10) -> Sair
                """);
    }
}
