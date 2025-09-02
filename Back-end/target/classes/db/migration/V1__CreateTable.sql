CREATE TABLE USUARIO (
                          id VARCHAR PRIMARY KEY,
                          nome VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          senha VARCHAR(255) NOT NULL,
                          data_nascimento DATE NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE PLANO (
                        id VARCHAR PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        preco DECIMAL(10,2) NOT NULL,
                        descricao TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE FILME (
                        id VARCHAR PRIMARY KEY,
                        titulo VARCHAR(255) NOT NULL,
                        descricao TEXT,
                        ano_lancamento INTEGER NOT NULL,
                        duracao_minutos INTEGER NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE SERIE (
                        id VARCHAR PRIMARY KEY,
                        titulo VARCHAR(255) NOT NULL,
                        descricao TEXT,
                        ano_lancamento INTEGER NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE PAGAMENTO (
                            id VARCHAR PRIMARY KEY,
                            id_usuario VARCHAR NOT NULL,
                            id_plano VARCHAR NOT NULL,
                            data_pagamento TIMESTAMP NOT NULL,
                            valor DECIMAL(10,2) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_pagamentos_usuario
                                FOREIGN KEY (id_usuario) REFERENCES USUARIO(id) ON DELETE CASCADE,
                            CONSTRAINT fk_pagamentos_plano
                                FOREIGN KEY (id_plano) REFERENCES PLANO(id) ON DELETE RESTRICT
);

CREATE TABLE AVALIACOE (
                            id VARCHAR PRIMARY KEY,
                            id_usuario VARCHAR NOT NULL,
                            id_filme VARCHAR NULL,
                            id_serie VARCHAR NULL,
                            nota INTEGER NOT NULL CHECK (nota >= 1 AND nota <= 5),
                            comentario TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_avaliacoes_usuario
                                FOREIGN KEY (id_usuario) REFERENCES USUARIO(id) ON DELETE CASCADE,
                            CONSTRAINT fk_avaliacoes_filme
                                FOREIGN KEY (id_filme) REFERENCES FILME(id) ON DELETE CASCADE,
                            CONSTRAINT fk_avaliacoes_serie
                                FOREIGN KEY (id_serie) REFERENCES SERIE(id) ON DELETE CASCADE,

                            CONSTRAINT chk_avaliacoes_filme_ou_serie
                                CHECK ((id_filme IS NOT NULL AND id_serie IS NULL) OR
                                       (id_filme IS NULL AND id_serie IS NOT NULL))
);

CREATE INDEX idx_usuarios_email ON USUARIO(email);
CREATE INDEX idx_pagamentos_usuario ON PAGAMENTO(id_usuario);
CREATE INDEX idx_pagamentos_plano ON PAGAMENTO(id_plano);
CREATE INDEX idx_pagamentos_data ON PAGAMENTO(data_pagamento);
CREATE INDEX idx_avaliacoes_usuario ON AVALIACOE(id_usuario);
CREATE INDEX idx_avaliacoes_filme ON AVALIACOE(id_filme);
CREATE INDEX idx_avaliacoes_serie ON AVALIACOE(id_serie);
CREATE INDEX idx_filmes_ano ON FILME(ano_lancamento);
CREATE INDEX idx_series_ano ON SERIE(ano_lancamento);

COMMENT ON TABLE USUARIO IS 'Tabela de usuários do sistema de streaming';
COMMENT ON TABLE PLANO IS 'Planos de assinatura disponíveis';
COMMENT ON TABLE PAGAMENTO IS 'Histórico de pagamentos dos usuários';
COMMENT ON TABLE FILME IS 'Catálogo de filmes disponíveis';
COMMENT ON TABLE SERIE IS 'Catálogo de séries disponíveis';
COMMENT ON TABLE AVALIACOE IS 'Avaliações dos usuários para filmes e séries';

COMMENT ON COLUMN AVALIACOE.nota IS 'Nota de 1 a 5 para o filme ou série';
COMMENT ON COLUMN AVALIACOE.id_filme IS 'Referência ao filme avaliado (NULL se for série)';
COMMENT ON COLUMN AVALIACOE.id_serie IS 'Referência à série avaliada (NULL se for filme)';

