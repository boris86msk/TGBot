CREATE TABLE public.users
(
    id serial primary key,
    tg_id integer,
    user_name character(100) NOT NULL,
    reg_date date

);