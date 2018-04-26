-- Required config data
insert into companies (name, description, industry, license_level, created_at, owner_id)
values ('Factory', 'The admin', 'IT', 3, now(), 1);

insert into roles (company_id, name, level, created_at, owner_id, can_create_task, can_create_product)
values (1, 'Super Admin', 0, now(), 1, true, true);

insert into users (username, password, remember, confirmed, salt, created_at, role_id, company_id, activated, name)
values ('synfm@factory.com', '$2a$10$/cqsHN1ECwO/HB20hZJO6.pmH3MfxgG/nrUyr2YpBnCwMrrGTZd.C', true, true, '$2a$10$s9816.SRCwXhP9Lk/GBqSO', now(), 1, 1, true, 'Super Admin');


-- Test data

insert into companies (name, description, industry, license_level, created_at, owner_id)
values ('Test companty', 'A company for testing only', 'ECE', 0, now(), 1);

insert into roles (company_id, name, level, created_at, owner_id, can_create_task, can_create_product)
values (2, 'Admin', 0, now(), 1, true, true);
insert into roles (company_id, name, level, created_at, owner_id, can_create_task, can_create_product)
values (2, 'Senior VP', 1, now(), 2, true, true);

insert into users (username, password, remember, confirmed, salt, created_at, role_id, company_id, registration_code, verification_needed, activated, name)
values ('test@company.com', '$2a$10$/cqsHN1ECwO/HB20hZJO6.pmH3MfxgG/nrUyr2YpBnCwMrrGTZd.C', true, true, '$2a$10$s9816.SRCwXhP9Lk/GBqSO', now(), 2, 2, '11B111', false, true, 'Xiao ming');
