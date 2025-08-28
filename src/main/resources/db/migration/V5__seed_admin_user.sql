SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT INTO usuarios (id, nome, cpf, email, telefone, senha_hash, role)
SELECT
  UUID_TO_BIN('00000000-0000-0000-0000-000000000002'),
  'Cliente de Souza',
  '000.000.000-00',
  'cliente.souza@email.com.br',
  '(99) 99999-9999',
  '!',
  'ADMIN'
WHERE NOT EXISTS (
  SELECT 1 FROM usuarios
  WHERE email = 'cliente.souza@email.com.br'
     OR cpf = '000.000.000-00'
);