insert into person (name, cpf)
values
    ('a test', '73478504872'),
    ('b test', '57683703745');

insert into account (cpf, balance)
values
    ('73478504872', 10),
    ('57683703745', 0);

insert into transaction (origin, destination, amount, created_at)
values
    (null, 1, 20, now() - 1),
    (null, 1, 10, now() - 10),
    (1, null, 10, now() - 50);