# Cafeteria Console

Sistema de console em Java para controlar produtos, estoque e pedidos de uma cafeteria, com dados salvos em PostgreSQL via JDBC.
Projeto de estudo de Java, POO e banco de dados, feito passo a passo.
Deixei o mapa mental que utilizei em PDF.

## Funcionalidades

- Cadastrar produto (nome, preço, estoque e categoria)
- Descadastrar produto (apenas se ele nunca apareceu em um pedido)
- Listar produtos
- Adicionar e remover estoque
- Criar pedido escolhendo os produtos pelo número, com subtotal e total
- Listar pedidos
- Painel de pedidos com as colunas **EM PREPARO** e **PRONTO**
- Marcar pedido como pronto

## Regras do sistema

- Não é possível remover do estoque mais do que existe.
- Não é possível pedir uma quantidade maior que o estoque disponível.
- Produtos esgotados não podem ser escolhidos em um pedido.
- Não existem dois produtos com o mesmo nome.
- Preço deve ser maior que zero e estoque não pode ser negativo.
- O pedido e a baixa de estoque são salvos juntos, em uma transação: se algo falhar, nada é gravado.
- O pedido guarda o preço do produto no momento da compra.
- Entradas inválidas (letras, números fora da faixa, texto vazio) são recusadas e a pergunta é repetida.

## Tecnologias

- Java (JDK 15 ou superior, pois o projeto usa text blocks)
- PostgreSQL e JDBC (driver na pasta `lib/`)
- IntelliJ IDEA
- Git

## Como rodar

1. Instale o **PostgreSQL** e crie o banco:
   ```sql
   CREATE DATABASE cafeteria_console;
   ```
2. Conectado a esse banco (pgAdmin, Query Tool), execute o script `sql/schema.sql`. Ele cria as tabelas e cadastra três produtos de exemplo.
3. Clone o repositório e abra a pasta no **IntelliJ IDEA**.
4. Adicione o driver ao projeto: clique com o botão direito em `lib/postgresql-*.jar` → **Add as Library...**
5. Defina a senha do PostgreSQL na variável de ambiente `DB_SENHA`: em **Run → Edit Configurations → Modify options → Environment variables**, escreva `DB_SENHA=sua_senha`.
6. Execute a classe `Main`.

Se o seu PostgreSQL usa outro usuário, porta ou nome de banco, ajuste a classe `banco/Conexao.java`.

## Estrutura do projeto

```
cafeteria_console/
├── lib/
│   └── postgresql-*.jar          # driver JDBC
├── sql/
│   └── schema.sql                # tabelas e dados de exemplo
└── src/
    ├── Main.java                 # menu principal e leitura de dados
    ├── banco/
    │   ├── Conexao.java          # conexão com o PostgreSQL
    │   ├── ProdutoRepositorio.java
    │   └── PedidoRepositorio.java
    ├── produto/
    │   ├── Produto.java
    │   └── Categoria.java        # enum: BEBIDA, COMIDA, SOBREMESA
    ├── estoque/
    │   ├── AdicionarEstoque.java
    │   └── RemoverEstoque.java
    ├── pedido/
    │   ├── Pedido.java
    │   ├── ItemPedido.java
    │   └── StatusPedido.java     # enum: EM_PREPARO, PRONTO
    └── menu/
        ├── Opcoes.java           # exibe o menu
        └── PainelPedidos.java    # painel de pedidos em duas colunas
```

## Conceitos praticados

- Classes, objetos, construtores e encapsulamento
- `ArrayList`, laço `for-each` e `enum`
- Menu com `Scanner`, `while` e `switch`
- Tratamento de entrada inválida com `try/catch`
- JDBC: `PreparedStatement`, `ResultSet` e transações (`commit` e `rollback`)
- Padrão repositório para separar o acesso ao banco do resto do código
- Organização do código em pacotes

## Limitações atuais

- Não é possível cancelar um pedido nem devolver o estoque.
- Produtos que já apareceram em pedidos não podem ser descadastrados.
- O painel de pedidos mostra todos os pedidos já feitos.

## Próximos passos

- [x] Java puro, POO, lista, menu e pedido
- [x] Persistência com PostgreSQL (JDBC)
- [ ] Spring Boot
- [ ] API REST testada com Postman
