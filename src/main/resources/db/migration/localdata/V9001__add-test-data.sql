insert into currency (id)
values ('GBP');

insert into currency (id)
values ('USD');

insert into exchange_rate (id, currency, rate)
values (uuid(), 'GBP', 1);

insert into exchange_rate (id, currency, rate)
values (uuid(), 'USD', 1.2);