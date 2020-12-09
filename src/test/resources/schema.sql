create table person (
    id identity,
    name varchar not null,
    cpf varchar unique not null
);

create table account (
    id identity,
    cpf varchar not null,
    balance decimal not null,
    constraint fk_account_cpf foreign key (cpf) references person(cpf)
);

create table transaction(
    id identity,
    origin int,
    destination int,
    amount decimal not null,
    created_at timestamp not null,
    constraint fk_transaction_origin foreign key (origin) references account(id),
    constraint fk_transaction_destination foreign key (destination) references account(id)
);

create index idx_transaction_origin on transaction(origin);
create index idx_transaction_destination on transaction(destination);

create view v_transaction_grouped as
  select t.origin, t.destination, sum(t.amount) as total
  from transaction t
  group by t.origin, t.destination;

create view v_debit as
  select acc.id as id, sum(g.total) as sum
  from account acc, v_transaction_grouped g
  where acc.id = g.origin
  group by acc.id;

create view v_credit as
  select acc.id as id, sum(g.total) as sum
  from account acc, v_transaction_grouped g
  where acc.id = g.destination
  group by acc.id;

create view v_curr_balance as
  select a.id as account_id,
         (  a.balance
            + coalesce(c.sum, 0)
            - coalesce(d.sum, 0)
         ) as balance
  from account a
  left join v_credit c
  on a.id = c.id
  left join v_debit d
  on a.id = d.id;