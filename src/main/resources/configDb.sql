create table companies (
	id serial primary key,
	name text not null,
	description text,
	industry text,
	licensed boolean not null default false,
	created_at timestamp without time zone NOT NULL
	updated_by integer
);

create table users (
	id serial primary key,
	username text not null,
	password text not null,
	access_token text,
	verification_code text,
	confirmed boolean not null default false,
	salt text not null,
	created_at timestamp without time zone NOT NULL,
	role_level integer not null,
	company_id integer not null REFERENCES companies,
	registration_code integer,
	updated_by integer,
	name text not null,
	phone text,
	work_id text,
	birthday timestamp without time zone,
	joined_date timestamp without time zone
);

create table roles (
	id serial primary key,
	company_id integer not null REFERENCES companies,
	name text not null,
	level integer not null,
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null REFERENCES users,
	updated_by integer REFERENCES users
);

create table products (
	id serial primary key,
	company_id integer not null REFERENCES companies,
	model text not null,
	name text not null,
	description text not null
	net_cost decimal,
	market_price decimal,
	price_visible_role_level integer,
	labor_cost decimal,
	
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null REFERENCES users,
	updated_by integer REFERENCES users
);

create table tasks (
	id serial primary key,
	company_id integer not null REFERENCES companies,
	title text not null,
	type text not null,
	description text,
	status text not null,
	visible_role_level integer,
	
	product_id integer REFERENCES products,
	count integer,
	
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null REFERENCES users,
	updated_by integer REFERENCES users,
	
	assignee integer,
	assigned_to_group boolean
);

create table task_assignee_groups (
	id serial primary key,
	task_id integer REFERENCES tasks
	assignee_id integer not null REFERENCES users,
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null REFERENCES users
);

create table images (

);
