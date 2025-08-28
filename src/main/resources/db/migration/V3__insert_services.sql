SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Corte: R$ 30,00, 30 min
INSERT INTO servicos (nome, duracao, preco)
SELECT 'Corte', 30, 30.00
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Corte');

-- Barba: R$ 30,00, 30 min
INSERT INTO servicos (nome, duracao, preco)
SELECT 'Barba', 30, 30.00
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Barba');

-- Corte + Barba: R$ 50,00, 90 min
INSERT INTO servicos (nome, duracao, preco)
SELECT 'Corte + Barba', 90, 50.00
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Corte + Barba');

-- Selagem: R$ 60,00, 60 min
INSERT INTO servicos (nome, duracao, preco)
SELECT 'Selagem', 60, 60.00
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Selagem');

-- Platinado: R$ 80,00, 120 min
INSERT INTO servicos (nome, duracao, preco)
SELECT 'Platinado', 120, 80.00
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE nome = 'Platinado');