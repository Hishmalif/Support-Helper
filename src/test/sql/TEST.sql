create schema tests;

-- Create test users
with test_users as (
	values ('Alexandr', 'Seleznev', 'hishmalif', 'en'),
	('Pavel', 'Annenkov', 'obcorp', 'fr'),
	('Ramazan', 'Gusenov', 'gus', 'az'),
	('Bogdan', 'Bashkir', 'useless_dude', 'kz')
)
insert into tests.users (telegram_id, name, login, language_code)
select 	row_number() over() as "telegram_id",
		tu.column1 || ' ' || tu2.column2 as "name",
		tu.column3 || '_' || tu2.column3 as "login",
		tu.column4 as "language_code"
from test_users tu
cross join test_users tu2;

-- Add test users to the block
insert into tests.blacklist (user_id, blocking_date, unlock_date, block_reason)
select u.id, now(), (now() + random() * interval '5 day'), 'TEST'
from tests.users u
cross join tests.users u2
where u.login like '%gus%' and u2.login like '%gus%'
