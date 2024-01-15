CREATE TABLE pagamento
(
    id     bigint      not null,
    status varchar(15) not null,
    primary key (id)

)engine = InnoDB default charset = UTF8MB4;


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