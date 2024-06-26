create schema if not exists catalog;

create table catalog.t_product
(
    id        serial primary key,
    c_name   varchar(50) not null check (length(trim(c_name)) >= 3),
    c_description varchar(1000)
);