create table companies (
	id serial primary key,
	name text not null,
	description text,
	industry text,
	licensed boolean not null default false,
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null,
	updated_by integer
);

create table roles (
	id serial primary key,
	company_id integer not null REFERENCES companies,
	name text not null,
	level integer not null,
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null,
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
	role_id integer not null REFERENCES roles,
	company_id integer not null REFERENCES companies,
	registration_code integer,
	updated_by integer,
	name text not null,
	phone text,
	work_id text,
	birthday timestamp without time zone,
	joined_date timestamp without time zone
);

create table products (
	id serial primary key,
	company_id integer not null REFERENCES companies,
	model text not null,
	name text not null,
	description text,
	combined_product boolean not null default false,
	net_cost decimal,
	market_price decimal,
	price_visible_role_id integer REFERENCES roles,
	labor_cost decimal,
	visible_role_id integer REFERENCES roles,
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null REFERENCES users,
	updated_by integer REFERENCES users
);

create table product_combinations (
	id serial primary key,
	parent_id integer not null REFERENCES products,
	child_id integer not null REFERENCES products,
	count integer not null,
	step integer not null,
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null REFERENCES users,
	updated_by integer REFERENCES users
);

create table task_assignee_groups (
	id serial primary key,
	task_id integer REFERENCES tasks
	assignee_id integer not null REFERENCES users,
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null REFERENCES users
);

create table tasks (
	id serial primary key,
	company_id integer not null REFERENCES companies,
	title text not null,
	type text not null,
	description text,
	status text not null,
	priority text not null,
	visible_role_id integer REFERENCES roles,
	assignee integer REFERENCES users,
	assignee_group_id integer REFERENCES task_assignee_groups,
	approver integer REFERENCES users,
	
	product_id integer REFERENCES products,
	count integer,
	
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null REFERENCES users,
	updated_by integer REFERENCES users,
	
);

create table task_results (
	
);

create table images (

);
