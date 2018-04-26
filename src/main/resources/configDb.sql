/* Cheat sheet
alter table companies alter column licensed drop default;
ALTER TABLE companies ALTER COLUMN licensed TYPE integer using licensed::integer;
ALTER TABLE companies RENAME licensed TO license_level;

DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
*/

create table companies (
	id serial primary key,
	name text not null UNIQUE,
	description text,
	industry text,
	license_level integer not null,
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
	updated_by integer,
	can_create_task boolean not null,
	can_create_product boolean not null
);

create table images (
	id serial primary key,
	company_id integer not null REFERENCES companies,
	type text,
	mapping_id integer,
	position integer,
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null
);

create table users (
	id serial primary key,
	username text not null UNIQUE,
	password text not null,
	access_token text,
	remember boolean not null,
	verification_code text,
	confirmed boolean not null default false,
	salt text not null,
	created_at timestamp without time zone NOT NULL
	updated_by integer,
	role_id integer not null REFERENCES roles,
	manager_id integer,
	company_id integer not null REFERENCES companies,
	registration_code text,
	verification_needed boolean,
	activated boolean,
	name text not null,
	phone text,
	work_id text,
	avatar_id integer REFERENCES images,
	birthday timestamp without time zone,
	joined_date timestamp without time zone
);

create table user_activations (
	id serial primary key,
	requester_id integer not null REFERENCES users,
	responder_id integer not null REFERENCES users,
	created_at timestamp without time zone NOT NULL
);

create table products (
	id serial primary key,
	company_id integer not null REFERENCES companies,
	model text not null,
	name text not null,
	description text,
	image_id integer REFERENCES images,
	combined_product boolean not null default false,
	labor_cost decimal,
	visible_role_id integer REFERENCES roles,
	sensitive_net_cost decimal,
	sensitive_market_price decimal,
	sensitive_description text,
	sensitive_visible_role_id integer REFERENCES roles,
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

create table error_logs (
	id serial primary key,
	url text,
	param text,
	trace text,
	created_at timestamp without time zone not null
);

-- Need to be deployed


-- Not deployed yet 

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

create table task_assignee_groups (
	id serial primary key,
	task_id integer REFERENCES tasks,
	assignee_id integer not null REFERENCES users,
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null REFERENCES users
);

create table task_results (
	
);


