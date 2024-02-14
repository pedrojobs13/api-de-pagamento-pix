create TABLE
    cliente
(
    id SERIAL PRIMARY KEY NOT NULL,
    nome      varchar(60)    not null,
    sobrenome varchar(60)    not null,
    email     varchar(120)   not null,
    valor     decimal(19, 2) not null

);

create TABLE
    produto
(
    id SERIAL PRIMARY KEY NOT NULL,
    title     varchar(80)    not null,
    descricao varchar(120),
    foto      varchar(120),
    valor     decimal(19, 2) not null
);
