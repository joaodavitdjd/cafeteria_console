package menu;

import java.util.ArrayList;
import java.util.List;
import pedido.Pedido;
import pedido.StatusPedido;

public class PainelPedidos {

    public void exibir(List<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado.");
            return;
        }

        List<String> emPreparo = new ArrayList<>();
        List<String> prontos = new ArrayList<>();

        for (Pedido p : pedidos) {
            String linha = String.format("#%03d %s", p.getNumero(), p.getCliente());
            if (linha.length() > 22) {
                linha = linha.substring(0, 22);
            }
            if (p.getStatus() == StatusPedido.PRONTO) {
                prontos.add(linha);
            } else {
                emPreparo.add(linha);
            }
        }

        System.out.println();
        System.out.println("========== PAINEL DE PEDIDOS ==========");
        System.out.println(String.format("%-24s| %s", " EM PREPARO", "PRONTO"));
        System.out.println("---------------------------------------");

        int linhas = Math.max(emPreparo.size(), prontos.size());
        for (int i = 0; i < linhas; i++) {
            String esquerda = i < emPreparo.size() ? emPreparo.get(i) : "";
            String direita = i < prontos.size() ? prontos.get(i) : "";
            System.out.println(String.format(" %-23s| %s", esquerda, direita));
        }
    }
}