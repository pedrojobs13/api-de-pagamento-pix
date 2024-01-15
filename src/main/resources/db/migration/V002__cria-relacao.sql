ALTER TABLE
    cliente
    ADD COLUMN
        produto_id bigint not null;

ALTER TABLE
    cliente
    ADD
        CONSTRAINT fk_produto_id FOREIGN KEY (produto_id) REFERENCES produto (id);