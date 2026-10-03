import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import estoque.AdicionarEstoque;
import estoque.RemoverEstoque;
import menu.Opcoes;
import pedido.StatusPedido;
import produto.Categoria;
import produto.Produto;
import pedido.Pedido;
import menu.PainelPedidos;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        AdicionarEstoque adicionar = new AdicionarEstoque();
        RemoverEstoque remover = new RemoverEstoque();
        Opcoes opc = new Opcoes();

        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto("Café Expresso", 5.00, 20, Categoria.BEBIDA));
        produtos.add(new Produto("Cappuccino", 8.50, 15, Categoria.BEBIDA));
        produtos.add(new Produto("Pão de Queijo", 4.50, 30, Categoria.COMIDA));

        List<Pedido> pedidos = new ArrayList<>();
        PainelPedidos painel = new PainelPedidos();

        int opcao = 0;

        while (opcao != 9) {
            opc.exibirOpcoes();
            opcao = lerInteiro(ler, "O que deseja fazer? ", 1, 9);

            switch (opcao) {
                case 1:
                    produtos.add(cadastrarProduto(ler, produtos));
                    System.out.println("Produto cadastrado!");
                    break;
                case 2:
                    Produto pAdd = pedirProduto(ler, produtos);
                    if (pAdd != null) {
                        int qtd = lerInteiro(ler, "Quantidade a adicionar: ", 1, Integer.MAX_VALUE);
                        adicionar.executar(pAdd, qtd);
                    }
                    break;
                case 3:
                    listarProdutos(produtos);
                    break;
                case 4:
                    Produto pRem = pedirProduto(ler, produtos);
                    if (pRem != null) {
                        int qtd = lerInteiro(ler, "Quantidade a remover: ", 1, Integer.MAX_VALUE);
                        remover.executar(pRem, qtd);
                    }
                    break;
                case 5:
                    Pedido novo = criarPedido(ler, produtos, pedidos.size() + 1);
                    if (novo != null) {
                        pedidos.add(novo);
                        System.out.println("\nPedido registrado!");
                        novo.exibirResumo();
                    }
                    break;
                case 6:
                    listarPedidos(pedidos);
                    break;
                case 7:
                    painel.exibir(pedidos);
                    break;
                case 8:
                    marcarPronto(ler, pedidos, painel);
                    break;
                case 9:
                    System.out.println("Saindo...");
                    break;
            }
        }
    }

    // ---------- leitura segura ----------
    public static int lerInteiro(Scanner ler, String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = Integer.parseInt(ler.nextLine().trim());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                if (max == Integer.MAX_VALUE) {
                    System.out.println("Digite um número maior ou igual a " + min + ".");
                } else {
                    System.out.println("Digite um número entre " + min + " e " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números inteiros.");
            }
        }
    }

    public static double lerPreco(Scanner ler, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                double valor = Double.parseDouble(ler.nextLine().trim().replace(",", "."));
                if (valor > 0) {
                    return valor;
                }
                System.out.println("O preço deve ser maior que zero.");
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor válido, por exemplo 5,50.");
            }
        }
    }

    public static String lerTexto(Scanner ler, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = ler.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("O texto não pode ficar vazio.");
        }
    }

    // ---------- produto ----------
    public static Produto cadastrarProduto(Scanner ler, List<Produto> produtos) {
        String nome;
        while (true) {
            nome = lerTexto(ler, "Qual o nome do produto? ");
            if (buscarProduto(produtos, nome) == null) {
                break;
            }
            System.out.println("Já existe um produto com esse nome.");
        }

        double preco = lerPreco(ler, "Qual o valor do produto? ");
        int estoque = lerInteiro(ler, "Qual o estoque do produto? ", 0, Integer.MAX_VALUE);

        Categoria[] categorias = Categoria.values();
        System.out.println("Categorias:");
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + " - " + categorias[i]);
        }
        int escolha = lerInteiro(ler, "Escolha a categoria: ", 1, categorias.length);

        return new Produto(nome, preco, estoque, categorias[escolha - 1]);
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

    public static Produto buscarProduto(List<Produto> produtos, String nome) {
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

    public static Produto pedirProduto(Scanner ler, List<Produto> produtos) {
        String nome = lerTexto(ler, "Nome do produto: ");
        Produto p = buscarProduto(produtos, nome);
        if (p == null) {
            System.out.println("Produto não encontrado.");
        }
        return p;
    }

    // ---------- pedido ----------
    public static Produto escolherProduto(Scanner ler, List<Produto> produtos) {
        System.out.println("\n--- PRODUTOS DISPONÍVEIS ---");
        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            String situacao = p.getEstoque() > 0 ? p.getEstoque() + " un" : "ESGOTADO";
            System.out.println(String.format("%d - %-20s R$ %6.2f  (%s)",
                    i + 1, p.getNome(), p.getPreco(), situacao));
        }
        System.out.println("0 - Voltar");

        while (true) {
            int escolha = lerInteiro(ler, "Número do produto: ", 0, produtos.size());
            if (escolha == 0) {
                return null;
            }
            Produto p = produtos.get(escolha - 1);
            if (p.getEstoque() == 0) {
                System.out.println("Produto esgotado. Escolha outro.");
            } else {
                return p;
            }
        }
    }

    public static Pedido criarPedido(Scanner ler, List<Produto> produtos, int numeroDoPedido) {
        if (produtos.isEmpty()) {
            System.out.println("Cadastre pelo menos um produto antes de criar um pedido.");
            return null;
        }

        Pedido pedido = new Pedido(numeroDoPedido, lerTexto(ler, "Nome do cliente: "));
        Produto p = null;
        int continuar = 1;

        while (continuar != 2) {
            // 1 = escolher outro produto | 3 = manter o mesmo produto e trocar a quantidade
            if (continuar == 1) {
                p = escolherProduto(ler, produtos);
            }

            boolean faltouEstoque = false;

            if (p != null) {
                int qtd = lerInteiro(ler, "Quantidade: ", 1, Integer.MAX_VALUE);
                if (pedido.adicionarItem(p, qtd)) {
                    System.out.println("Item adicionado!");
                } else {
                    System.out.println("Estoque insuficiente! Só tem " + p.getEstoque() + " un no estoque.");
                    faltouEstoque = true;
                }
            }

            if (faltouEstoque) {
                continuar = lerInteiro(ler,
                        "O que deseja fazer? (1 - outro produto, 2 - finalizar, 3 - outra quantidade): ", 1, 3);
            } else {
                continuar = lerInteiro(ler, "Adicionar outro item? (1 - sim, 2 - não): ", 1, 2);
            }
        }

        if (pedido.estaVazio()) {
            System.out.println("Pedido sem itens, cancelado.");
            return null;
        }
        return pedido;
    }

    public static void listarPedidos(List<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado.");
            return;
        }
        System.out.println("--- Pedidos ---");
        for (Pedido p : pedidos) {
            System.out.println();
            p.exibirResumo();
        }
    }

    public static void marcarPronto(Scanner ler, List<Pedido> pedidos, PainelPedidos painel) {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado.");
            return;
        }

        painel.exibir(pedidos);
        int numero = lerInteiro(ler, "\nNúmero do pedido que ficou pronto (0 para voltar): ", 0, pedidos.size());
        if (numero == 0) {
            return;
        }

        Pedido p = pedidos.get(numero - 1);
        if (p.getStatus() == StatusPedido.PRONTO) {
            System.out.println("Esse pedido já está pronto.");
            return;
        }

        p.marcarComoPronto();
        System.out.println(String.format("Pedido #%03d pronto para retirada!", p.getNumero()));
    }
}