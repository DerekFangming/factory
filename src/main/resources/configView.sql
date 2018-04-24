
-- Not deployed yet 
create or replace view user_details as 
select (select level from roles where id = users.id), *
from users;