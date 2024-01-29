alter table cliente add codigo varchar(36) not null after id;
update cliente set codigo = uuid();


alter table pagamento add codigo varchar(36) not null after id;
update pagamento set codigo = uuid();