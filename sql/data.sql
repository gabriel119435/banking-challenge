insert into person (name, cpf)
values
    ('a', '73478504872'),
    ('b', '57683703745');

insert into account (cpf, balance)
values
    ('73478504872', 10),
    ('57683703745', 0);

insert into transaction (origin, destination, amount, created_at)
values
    (null, 1, 20, now() - interval '1 day'),
    (null, 1, 10, now() - interval '10 day'),
    (1, null, 10, now() - interval '50 day');