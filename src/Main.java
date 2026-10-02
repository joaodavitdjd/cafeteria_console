import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import produto.Produto;
import menu.Opcoes;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
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
                case 3:
                    listarProdutos(produtos);
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
        for (Produto p : produtos) {
            p.exibirInformacoes();
        }
    }
}