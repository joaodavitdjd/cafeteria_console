-- Cafeteria Console - estrutura do banco de dados (PostgreSQL)
--
-- Antes de rodar este script, crie o banco (uma vez só):
--     CREATE DATABASE cafeteria_console;
-- Depois conecte-se a ele e execute este arquivo.

CREATE TABLE IF NOT EXISTS produtos (
    id        SERIAL PRIMARY KEY,
    nome      VARCHAR(100)  NOT NULL UNIQUE,
    preco     NUMERIC(10,2) NOT NULL CHECK (preco > 0),
    estoque   INTEGER       NOT NULL CHECK (estoque >= 0),
    categoria VARCHAR(20)   NOT NULL CHECK (categoria IN ('BEBIDA', 'COMIDA', 'SOBREMESA'))
);

CREATE TABLE IF NOT EXISTS pedidos (
    id      SERIAL PRIMARY KEY,
    cliente VARCHAR(100) NOT NULL,
    status  VARCHAR(20)  NOT NULL DEFAULT 'EM_PREPARO'
            CHECK (status IN ('EM_PREPARO', 'PRONTO'))
);

CREATE TABLE IF NOT EXISTS itens_pedido (
    id             SERIAL PRIMARY KEY,
    pedido_id      INTEGER       NOT NULL REFERENCES pedidos(id),
    produto_id     INTEGER       NOT NULL REFERENCES produtos(id),
    quantidade     INTEGER       NOT NULL CHECK (quantidade > 0),
    preco_unitario NUMERIC(10,2) NOT NULL
);

-- Produtos de exemplo (opcional)
INSERT INTO produtos (nome, preco, estoque, categoria) VALUES
    ('Café Expresso', 5.00, 20, 'BEBIDA'),
    ('Cappuccino', 8.50, 15, 'BEBIDA'),
    ('Pão de Queijo', 4.50, 30, 'COMIDA')
ON CONFLICT (nome) DO NOTHING;
