SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

-- João Lucas
INSERT INTO barbeiros (nome, cpf, telefone)
SELECT 'João Lucas', '23165498701', '(11) 91234-5678'
WHERE NOT EXISTS (SELECT 1 FROM barbeiros WHERE cpf = '23165498701');

-- David Arnaldo
INSERT INTO barbeiros (nome, cpf, telefone)
SELECT 'David Arnaldo', '50291374685', '(21) 99876-5432'
WHERE NOT EXISTS (SELECT 1 FROM barbeiros WHERE cpf = '50291374685');

-- Melissa Borges
INSERT INTO barbeiros (nome, cpf, telefone)
SELECT 'Melissa Borges', '90431726540', '(62) 98123-4567'
WHERE NOT EXISTS (SELECT 1 FROM barbeiros WHERE cpf = '90431726540');

-- Pedro Arguiar
INSERT INTO barbeiros (nome, cpf, telefone)
SELECT 'Pedro Arguiar', '13820579462', '(31) 99700-1122'
WHERE NOT EXISTS (SELECT 1 FROM barbeiros WHERE cpf = '13820579462');

-- Taynara Marques
INSERT INTO barbeiros (nome, cpf, telefone)
SELECT 'Taynara Marques', '74621930587', '(85) 98888-0001'
WHERE NOT EXISTS (SELECT 1 FROM barbeiros WHERE cpf = '74621930587');