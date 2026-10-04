import banco.PedidoRepositorio;
import banco.ProdutoRepositorio;
import estoque.AdicionarEstoque;
import estoque.RemoverEstoque;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import menu.Opcoes;
import menu.PainelPedidos;
import pedido.Pedido;
import pedido.StatusPedido;
import produto.Categoria;
import produto.Produto;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        AdicionarEstoque adicionar = new AdicionarEstoque();
        RemoverEstoque remover = new RemoverEstoque();
        Opcoes opc = new Opcoes();
        PainelPedidos painel = new PainelPedidos();
        ProdutoRepositorio repo = new ProdutoRepositorio();
        PedidoRepositorio pedidoRepo = new PedidoRepositorio();
        int opcao = 0;

        while (opcao != 10) {
            opc.exibirOpcoes();
            opcao = lerInteiro(ler, "O que deseja fazer? ", 1, 10);

            try {
                switch (opcao) {
                    case 1:
                        repo.salvar(cadastrarProduto(ler, repo));
                        System.out.println("Produto cadastrado!");
                        break;
                    case 2:
                        descadastrarProduto(ler, repo);
                        break;
                    case 3:
                        listarProdutos(repo);
                        break;
                    case 4:
                        Produto pAdd = pedirProduto(ler, repo);
                        if (pAdd != null) {
                            int qtd = lerInteiro(ler, "Quantidade a adicionar: ", 1, Integer.MAX_VALUE);
                            adicionar.executar(pAdd, qtd);
                            repo.atualizarEstoque(pAdd);
                        }
                        break;
                    case 5:
                        Produto pRem = pedirProduto(ler, repo);
                        if (pRem != null) {
                            int qtd = lerInteiro(ler, "Quantidade a remover: ", 1, Integer.MAX_VALUE);
                            remover.executar(pRem, qtd);
                            repo.atualizarEstoque(pRem);
                        }
                        break;
                    case 6:
                        Pedido novo = criarPedido(ler, repo);
                        if (novo != null) {
                            pedidoRepo.salvar(novo);
                            System.out.println("\nPedido registrado!");
                            novo.exibirResumo();
                        }
                        break;
                    case 7:
                        listarPedidos(pedidoRepo);
                        break;
                    case 8:
                        painel.exibir(pedidoRepo.listarTodos());
                        break;
                    case 9:
                        marcarPronto(ler, pedidoRepo, painel);
                        break;
                    case 10:
                        System.out.println("Saindo...");
                        break;
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
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
    public static Produto cadastrarProduto(Scanner ler, ProdutoRepositorio repo) throws SQLException {
        String nome;
        while (true) {
            nome = lerTexto(ler, "Qual o nome do produto? ");
            if (repo.buscarPorNome(nome) == null) {
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

    public static void listarProdutos(ProdutoRepositorio repo) throws SQLException {
        List<Produto> produtos = repo.listarTodos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        System.out.println("--- Produtos cadastrados ---");
        for (Produto p : produtos) {
            p.exibirInformacoes();
        }
    }

    public static Produto pedirProduto(Scanner ler, ProdutoRepositorio repo) throws SQLException {
        String nome = lerTexto(ler, "Nome do produto: ");
        Produto p = repo.buscarPorNome(nome);
        if (p == null) {
            System.out.println("Produto não encontrado.");
        }
        return p;
    }
    public static void descadastrarProduto(Scanner ler, ProdutoRepositorio repo) throws SQLException {
        List<Produto> produtos = repo.listarTodos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.println("\n--- DESCADASTRAR PRODUTO ---");
        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            System.out.println(String.format("%d - %-20s R$ %6.2f  (%d un)",
                    i + 1, p.getNome(), p.getPreco(), p.getEstoque()));
        }
        System.out.println("0 - Voltar");

        int escolha = lerInteiro(ler, "Número do produto: ", 0, produtos.size());
        if (escolha == 0) {
            return;
        }
        Produto escolhido = produtos.get(escolha - 1);

        if (repo.temPedidos(escolhido)) {
            System.out.println("Não é possível descadastrar: esse produto já aparece em pedidos.");
            return;
        }

        int confirmar = lerInteiro(ler, "Descadastrar " + escolhido.getNome() + "? (1 - sim, 2 - não): ", 1, 2);
        if (confirmar == 1) {
            repo.excluir(escolhido);
            System.out.println("Produto descadastrado!");
        }
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

    public static Pedido criarPedido(Scanner ler, ProdutoRepositorio repo) throws SQLException {
        // a lista é carregada UMA vez e usada em todo o pedido, para o estoque
        // mostrado levar em conta o que o cliente já pediu
        List<Produto> produtos = repo.listarTodos();
        if (produtos.isEmpty()) {
            System.out.println("Não há produtos cadastrados. Use a opção 1 antes de criar um pedido.");
            return null;
        }

        Pedido pedido = new Pedido(lerTexto(ler, "Nome do cliente: "));
        Produto p = null;
        int continuar = 1;

        while (continuar != 2) {
            if (continuar == 1) {
                p = escolherProduto(ler, produtos);
            }

            boolean faltouEstoque = false;

            if (p != null) {
                int qtd = lerInteiro(ler, "Quantidade: ", 1, Integer.MAX_VALUE);
                if (pedido.adicionarItem(p, qtd)) {
                    System.out.println("Item adicionado!");
                } else {
                    System.out.println("Estoque insuficiente! Só tem " + p.getEstoque() + " un.");
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

    public static void listarPedidos(PedidoRepositorio pedidoRepo) throws SQLException {
        List<Pedido> pedidos = pedidoRepo.listarTodos();
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

    public static void marcarPronto(Scanner ler, PedidoRepositorio pedidoRepo, PainelPedidos painel)
            throws SQLException {
        List<Pedido> pedidos = pedidoRepo.listarTodos();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado.");
            return;
        }

        painel.exibir(pedidos);
        int numero = lerInteiro(ler, "\nNúmero do pedido que ficou pronto (0 para voltar): ", 0, Integer.MAX_VALUE);
        if (numero == 0) {
            return;
        }

        Pedido escolhido = null;
        for (Pedido p : pedidos) {
            if (p.getNumero() == numero) {
                escolhido = p;
                break;
            }
        }

        if (escolhido == null) {
            System.out.println("Pedido não encontrado.");
            return;
        }
        if (escolhido.getStatus() == StatusPedido.PRONTO) {
            System.out.println("Esse pedido já está pronto.");
            return;
        }

        escolhido.marcarComoPronto();
        pedidoRepo.atualizarStatus(escolhido);
        System.out.println(String.format("Pedido #%03d pronto para retirada!", escolhido.getNumero()));
    }
}