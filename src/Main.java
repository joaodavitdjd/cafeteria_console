import java.util.Scanner;
import Produto.Produto;

public class Main {
    public static void main(String[] args) {

        Scanner ler = new Scanner(System.in);
        Produto Prod = new Produto();

        System.out.print("qual o nome do produto ");
        Prod.nome = ler.nextLine();
        System.out.print("qual o valor do produto ");
        Prod.preco = ler.nextDouble();
        System.out.print("qual a quantidade do produto ");
        Prod.quantidade = ler.nextInt();

        System.out.println("o nome do produto é: " + Prod.nome + ", o valor do produto é: " + Prod.preco +
                " R$, a quantidade do produto é: " + Prod.quantidade + "un");
    }
}