SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT INTO usuarios (id, nome, cpf, email, telefone, senha_hash, role)
SELECT
  UUID_TO_BIN('00000000-0000-0000-0000-000000000001'),
  'Convidado',
  '00000000000',
  'guest@barberbook.local',
  '000000000',
  '!',
  'CLIENTE'
WHERE NOT EXISTS (
  SELECT 1 FROM usuarios
  WHERE id = UUID_TO_BIN('00000000-0000-0000-0000-000000000001')
     OR email = 'guest@barberbook.local'
);