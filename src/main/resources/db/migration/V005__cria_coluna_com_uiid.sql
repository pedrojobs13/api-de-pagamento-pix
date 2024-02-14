alter table cliente add codigo varchar(36) not null;
update cliente set codigo = gen_random_uuid();


alter table pagamento add codigo varchar(36) not null;
update pagamento set codigo = gen_random_uuid();