CREATE TABLE USUARIOS (
                          id SERIAL PRIMARY KEY,
                          nome VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          senha VARCHAR(255) NOT NULL,
                          data_nascimento DATE NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE PLANOS (
                        id SERIAL PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL,
                        preco DECIMAL(10,2) NOT NULL,
                        descricao TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE FILMES (
                        id SERIAL PRIMARY KEY,
                        titulo VARCHAR(255) NOT NULL,
                        descricao TEXT,
                        ano_lancamento INTEGER NOT NULL,
                        duracao_minutos INTEGER NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE SERIES (
                        id SERIAL PRIMARY KEY,
                        titulo VARCHAR(255) NOT NULL,
                        descricao TEXT,
                        ano_lancamento INTEGER NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE PAGAMENTOS (
                            id SERIAL PRIMARY KEY,
                            id_usuario INTEGER NOT NULL,
                            id_plano INTEGER NOT NULL,
                            data_pagamento TIMESTAMP NOT NULL,
                            valor DECIMAL(10,2) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_pagamentos_usuario
                                FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id) ON DELETE CASCADE,
                            CONSTRAINT fk_pagamentos_plano
                                FOREIGN KEY (id_plano) REFERENCES PLANOS(id) ON DELETE RESTRICT
);

CREATE TABLE AVALIACOES (
                            id SERIAL PRIMARY KEY,
                            id_usuario INTEGER NOT NULL,
                            id_filme INTEGER NULL,
                            id_serie INTEGER NULL,
                            nota INTEGER NOT NULL CHECK (nota >= 1 AND nota <= 5),
                            comentario TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_avaliacoes_usuario
                                FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id) ON DELETE CASCADE,
                            CONSTRAINT fk_avaliacoes_filme
                                FOREIGN KEY (id_filme) REFERENCES FILMES(id) ON DELETE CASCADE,
                            CONSTRAINT fk_avaliacoes_serie
                                FOREIGN KEY (id_serie) REFERENCES SERIES(id) ON DELETE CASCADE,

                            CONSTRAINT chk_avaliacoes_filme_ou_serie
                                CHECK ((id_filme IS NOT NULL AND id_serie IS NULL) OR
                                       (id_filme IS NULL AND id_serie IS NOT NULL))
);

CREATE INDEX idx_usuarios_email ON USUARIOS(email);
CREATE INDEX idx_pagamentos_usuario ON PAGAMENTOS(id_usuario);
CREATE INDEX idx_pagamentos_plano ON PAGAMENTOS(id_plano);
CREATE INDEX idx_pagamentos_data ON PAGAMENTOS(data_pagamento);
CREATE INDEX idx_avaliacoes_usuario ON AVALIACOES(id_usuario);
CREATE INDEX idx_avaliacoes_filme ON AVALIACOES(id_filme);
CREATE INDEX idx_avaliacoes_serie ON AVALIACOES(id_serie);
CREATE INDEX idx_filmes_ano ON FILMES(ano_lancamento);
CREATE INDEX idx_series_ano ON SERIES(ano_lancamento);

COMMENT ON TABLE USUARIOS IS 'Tabela de usuários do sistema de streaming';
COMMENT ON TABLE PLANOS IS 'Planos de assinatura disponíveis';
COMMENT ON TABLE PAGAMENTOS IS 'Histórico de pagamentos dos usuários';
COMMENT ON TABLE FILMES IS 'Catálogo de filmes disponíveis';
COMMENT ON TABLE SERIES IS 'Catálogo de séries disponíveis';
COMMENT ON TABLE AVALIACOES IS 'Avaliações dos usuários para filmes e séries';

COMMENT ON COLUMN AVALIACOES.nota IS 'Nota de 1 a 5 para o filme ou série';
COMMENT ON COLUMN AVALIACOES.id_filme IS 'Referência ao filme avaliado (NULL se for série)';
COMMENT ON COLUMN AVALIACOES.id_serie IS 'Referência à série avaliada (NULL se for filme)';

