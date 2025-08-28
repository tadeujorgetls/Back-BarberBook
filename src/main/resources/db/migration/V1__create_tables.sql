SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET sql_notes = 0;

-- =========================
-- Tabela: usuarios
-- =========================
CREATE TABLE IF NOT EXISTS usuarios (
  id            BINARY(16)    NOT NULL DEFAULT (UUID_TO_BIN(UUID())),
  nome          VARCHAR(255)  NOT NULL,
  cpf           VARCHAR(14)   NOT NULL,
  email         VARCHAR(255)  NOT NULL,
  telefone      VARCHAR(20)   NOT NULL,
  senha_hash    VARCHAR(255)  NOT NULL,
  role          ENUM('CLIENTE','ADMIN') NOT NULL DEFAULT 'CLIENTE',
  created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT pk_usuarios PRIMARY KEY (id),
  CONSTRAINT uk_usuarios_email UNIQUE (email),
  CONSTRAINT uk_usuarios_cpf   UNIQUE (cpf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================
-- Tabela: barbeiros
-- =========================
CREATE TABLE IF NOT EXISTS barbeiros (
  id            BINARY(16)    NOT NULL DEFAULT (UUID_TO_BIN(UUID())),
  nome          VARCHAR(255)  NOT NULL,
  cpf           VARCHAR(14)   NOT NULL,
  telefone      VARCHAR(20)   NOT NULL,
  foto_url      VARCHAR(512)  NULL,
  created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT pk_barbeiros PRIMARY KEY (id),
  CONSTRAINT uk_barbeiros_cpf UNIQUE (cpf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================
-- Tabela: servicos  (UUID)
-- =========================
CREATE TABLE IF NOT EXISTS servicos (
  id            BINARY(16)    NOT NULL DEFAULT (UUID_TO_BIN(UUID())),
  nome          VARCHAR(120)  NOT NULL,
  duracao       INT           NOT NULL COMMENT 'em minutos',
  preco         DECIMAL(10,2) NOT NULL,
  created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT pk_servicos PRIMARY KEY (id),
  CONSTRAINT uk_servicos_nome UNIQUE (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================
-- Tabela: agendamentos  (UUID)
-- =========================
CREATE TABLE IF NOT EXISTS agendamentos (
  id              BINARY(16)    NOT NULL DEFAULT (UUID_TO_BIN(UUID())),
  user_id         BINARY(16)    NOT NULL,
  servico_id      BINARY(16)    NOT NULL,
  barbeiro_id     BINARY(16)    NOT NULL,
  data_horario    DATETIME      NOT NULL,
  status          ENUM('PENDENTE','CONCLUIDO','CANCELADO') NOT NULL DEFAULT 'PENDENTE',
  forma_pagamento ENUM('LOJA','ONLINE','CARTAO','DINHEIRO') NOT NULL DEFAULT 'LOJA',
  created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT pk_agendamentos PRIMARY KEY (id),

  CONSTRAINT fk_ag_user
    FOREIGN KEY (user_id)     REFERENCES usuarios(id)
    ON DELETE CASCADE  ON UPDATE CASCADE,

  CONSTRAINT fk_ag_servico
    FOREIGN KEY (servico_id)  REFERENCES servicos(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,

  CONSTRAINT fk_ag_barbeiro
    FOREIGN KEY (barbeiro_id) REFERENCES barbeiros(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,

  KEY idx_ag_user_data (user_id, data_horario),
  KEY idx_ag_barbeiro_data (barbeiro_id, data_horario),

  CONSTRAINT uk_ag_barbeiro_inicio UNIQUE (barbeiro_id, data_horario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET sql_notes = 1;