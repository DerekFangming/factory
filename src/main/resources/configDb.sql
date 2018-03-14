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
	role_id integer not null,
	company_id integer not null,
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
	company_id integer not null,
	
);