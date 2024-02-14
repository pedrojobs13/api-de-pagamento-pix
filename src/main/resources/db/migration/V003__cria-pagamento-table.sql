CREATE TABLE pagamento
(
    id SERIAL PRIMARY KEY NOT NULL,
    id_pagamento bigint      not null,
    status       varchar(15) not null
);


ALTER TABLE
    pagamento
    ADD COLUMN
        fk_cliente_id bigint not null;


ALTER TABLE
    pagamento
    ADD CONSTRAINT
        fk_cliente_id
        FOREIGN KEY
            (fk_cliente_id)
            REFERENCES
                cliente (id);