create TABLE
    cliente
(
    id        bigint         not null auto_increment,
    nome      varchar(60)    not null,
    sobrenome varchar(60)    not null,
    email     varchar(120)   not null,
    valor     decimal(19, 2) not null,


    primary key (id)

) engine = InnoDB default charset = UTF8MB4;

create TABLE
    produto
(
    id        bigint         not null auto_increment,
    title     varchar(80)    not null,
    descricao varchar(120),
    foto      varchar(120),
    valor     decimal(19, 2) not null,


    primary key (id)
) engine = InnoDB default charset = UTF8MB4;
