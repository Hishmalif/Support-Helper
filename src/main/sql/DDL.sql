create schema if not exists metrics;

-- Create table for users
create table if not exists public.users(
    id            bigserial primary key,
    telegram_id   bigint not null,
    name          text,
    login         text,
    active		  bool not null default true,
    first_login	  timestamp with time zone not null default now(),
    language_code varchar(20),
    is_bot        bool not null default false,
    is_premium    bool not null default false,
    is_admin      bool not null default false,
    constraint ix_users_telegramid unique (telegram_id)
);
create index if not exists ix_users_login on public.users(login);

-- Create table for blacklist
create table if not exists public.blacklist(
	user_id bigint references users(id),
	blocking_date timestamp with time zone not null default now(),
	unlock_date timestamp with time zone not null,
	block_reason text not null,
	constraint ck_blacklist_unlockdate_validate check (unlock_date > blocking_date and unlock_date > now())
);
create index if not exists ix_blacklist_userid on public.blacklist(user_id);
create index if not exists ix_blacklist_unlockdate on public.blacklist(unlock_date);

-- Create table for personality users data
create table if not exists public.user_data(
	user_id bigint references users(id),
	key text not null,
	value text not null,
	constraint ix_user_data_userid_key unique (user_id, key)
);
create index if not exists ix_user_data_userid on public.user_data(user_id);

-- Trigger for deactivation when user in block
create or replace function deactivation_block_user()
returns trigger
language 'plpgsql'
as $$
begin
	update public.users
	set active = false
	where id = new.user_id;
	return null;
end;
$$;

create or replace trigger tr_deactivation_user
after insert on public.blacklist
for each row
execute function deactivation_block_user();

-- Trigger for encode personality user data
create or replace function encode_user_data_value()
returns trigger
language 'plpgsql'
as $$
begin
	update public.user_data
	set value = encode(new.value::bytea, 'base64') 
	where user_id = new.user_id and "key" = new."key";
	return null;
end;
$$;

create or replace trigger tr_encode_user_data_value
after insert on public.user_data
for each row
execute function encode_user_data_value();

-- Create table for enums the metrics
create table if not exists metrics.commands(
	id serial primary key,
	name varchar(100) not null,
	description text,
	state bool not null default true
);

-- Create table for statistics of usage methods in the app
create table if not exists metrics.user_usage(
	user_id bigint references public.users(id),
	command_id integer references metrics.commands(id),
	usage_date timestamp with time zone not null default now()
);
create index if not exists ix_user_usage_userid on metrics.user_usage(user_id);
create index if not exists ix_user_usage_commandid on metrics.user_usage(command_id);
create index if not exists ix_user_usage_usagedate on metrics.user_usage(usage_date);