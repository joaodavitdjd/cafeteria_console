import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import estoque.AdicionarEstoque;
import estoque.RemoverEstoque;
import menu.Opcoes;
import produto.Produto;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        AdicionarEstoque adicionar = new AdicionarEstoque();
        RemoverEstoque remover = new RemoverEstoque();
    // ----------   menu  ----------
        Opcoes opc = new Opcoes();
        List<Produto> produtos = new ArrayList<>();
        int opcao = 0;

        while (opcao != 5) {
            opc.exibirOpcoes();
            opcao = Integer.parseInt(ler.nextLine());

            switch (opcao) {
                case 1:
                    produtos.add(cadastrarProduto(ler));
                    System.out.println("Produto cadastrado!");
                    break;
                case 2:
                    Produto pAdd = pedirProduto(ler, produtos);
                    if (pAdd != null) {
                        System.out.print("Quantidade a adicionar: ");
                        adicionar.executar(pAdd, Integer.parseInt(ler.nextLine()));
                    }
                    break;
                case 3:
                    listarProdutos(produtos);
                    break;
                case 4:
                    Produto pRem = pedirProduto(ler, produtos);
                    if (pRem != null) {
                        System.out.print("Quantidade a remover: ");
                        remover.executar(pRem, Integer.parseInt(ler.nextLine()));
                    }
                    break;
                case 5:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção ainda não feita ou inválida!");
            }
        }
    }
    // ----------   menu  ----------

    // ---------- produto ----------
    public static Produto cadastrarProduto(Scanner ler) {
        System.out.print("Qual o nome do produto? ");
        String nome = ler.nextLine();
        System.out.print("Qual o valor do produto? ");
        double preco = Double.parseDouble(ler.nextLine());
        System.out.print("Qual o estoque do produto? ");
        int estoque = Integer.parseInt(ler.nextLine());

        return new Produto(nome, preco, estoque);
    }

    public static void listarProdutos(List<Produto> produtos) {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        System.out.println("--- Produtos cadastrados ---");
        for (Produto p : produtos) {
            p.exibirInformacoes();
        }
    }
    // ---------- produto ----------

    // ---------- adicionar_e_remover_estoque ----------

    public static Produto pedirProduto(Scanner ler, List<Produto> produtos) {
        System.out.print("Nome do produto: ");
        String nome = ler.nextLine();

        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        System.out.println("Produto não encontrado.");
        return null;
    }


    // ---------- adicionar_e_remover_estoque ----------

}