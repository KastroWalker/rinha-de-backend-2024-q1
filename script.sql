CREATE TABLE IF NOT EXISTS clientes(
	id SERIAL PRIMARY KEY,
	nome VARCHAR NOT NULL,
	limite INT NOT NULL,
	saldo INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transacoes(
	id SERIAL PRIMARY KEY,
	valor INT NOT NULL,
	tipo VARCHAR NOT NULL,
	descricao VARCHAR NOT NULL,
	realizada_em TIMESTAMP NOT NULL,
	cliente_id INT NOT NULL,
	FOREIGN KEY(cliente_id) REFERENCES clientes(id)
);

DO $$
BEGIN
  INSERT INTO clientes(nome, limite)
  VALUES
    ('o barato sai caro', 1000 * 100),
    ('zan corp ltda', 800 * 100),
    ('les cruders', 10000 * 100),
    ('padaria joia de cocaia', 100000 * 100),
    ('kid mais', 5000 * 100);
END; $$
