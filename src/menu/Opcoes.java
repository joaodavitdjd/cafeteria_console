package menu;

public class Opcoes {
    public void exibirOpcoes() {
        System.out.print("""
                ===== CAFETERIA =====
                (1) -> cadastrar produto
                (2) -> Adicionar estoque
                (3) -> listar produtos
                (4) -> remover estoque
                (5) -> sair
                """);
        System.out.print("oque deseja fazer ? ");
    }
}
